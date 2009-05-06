require 'socket'

begin
  # ssanders: Try to load RSpec based on the LOADPATH, this allows
  #           projects (e.g. Rails) to provide alternate versions via the buildpath
  require 'spec'
rescue LoadError
  # ssanders: Fallback to loading from the Gem
  require 'rubygems'

  gem 'rspec'
  require 'spec'
end
require 'spec/runner/formatter/base_formatter'

module Spec
	module Example
		class ExampleGroup
			alias_method :initialize_old, :initialize
			def initialize(*args, &block)
				result = initialize_old(*args, &block)
				# ssanders: Override for "pending" examples
				unless block
					@from = caller
					while !@from.empty? && /.*`it'/ !~ @from.first
						@from.shift
					end
					@from.shift
				end
				result
			end

			def implementation_backtrace
				if @from
					@from
				else
					super
				end
			end
		end

		module ExampleMethods
			IN_METHOD_RE = /^(.+):in `(.+)'$/
			def rspecTestName
				if @DLTK_backtrace.nil?

					# tgrimm: This a workaround for a bug in 1.1.12:
					filtered_backtrace = respond_to?(:backtrace) ? backtrace : implementation_backtrace
					filtered_backtrace = filtered_backtrace.reject { |bt| bt =~ /(example_group_methods|dltk-rspec-runner.rb)/ }

					@DLTK_backtrace = filtered_backtrace[0]
					if @DLTK_backtrace =~ IN_METHOD_RE 
						@DLTK_backtrace = $1
					end
				end
				description + '<' + @DLTK_backtrace
			end
		end
	end
end

module Spec
	module Example
		module ExampleGroupMethods
			def DLTK_examples_to_run
				examples_to_run
			end
		end
	end
end		

unless ::Spec::VERSION::MAJOR > 1 || ::Spec::VERSION::MINOR > 0
	module Spec
		module DSL
			class Example
				alias_method :initialize_old, :initialize
				def initialize(*args, &block)
					result = initialize_old(*args, &block)
					@from = caller(0)[3]
					Description.description.examples << self
					result
				end
				IN_METHOD_RE = /^(.+):in `(.+)'$/
				def rspecTestName
					if @DLTK_backtrace.nil?
						backtrace = @from
						if backtrace =~ IN_METHOD_RE 
							backtrace = $1
						end
						@DLTK_backtrace = backtrace
					end
					description + '<' + @DLTK_backtrace
				end
			end
		end
	end

	module Spec
		module DSL
			class Description
				def self.description
					@@description
				end
				alias_method :initialize_old, :initialize
				def initialize(*args, &block)
					result = initialize_old(*args, &block)
					@@description = self
					result
				end
				def description_text
					description
				end
				def examples
					@examples ||= []
				end
			end
		end
	end
end

module DLTK
	module RSpec
		module EnvVars
			# environment variable name to pass communication port number
			# to the launched script
			PORT = "RUBY_TESTING_PORT"
			PATH = "RUBY_TESTING_PATH"
		end # of EnvVars

		module MessageIds
			# Notification that a test run has started. 
			# MessageIds.TEST_RUN_START + testCount.toString + " " + version
			TEST_RUN_START = "%TESTC  "

			# Notification that a test run has ended.
			# TEST_RUN_END + elapsedTime.toString().
			TEST_RUN_END   = "%RUNTIME"

			# Notification about a test inside the test suite.
			# TEST_TREE + testId + "," + testName + "," + isSuite + "," + testcount
			# isSuite = "true" or "false"
			TEST_TREE      = "%TSTTREE"

			#Notification that a test has started.
			# MessageIds.TEST_START + testID + "," + testName
			TEST_START     = "%TESTS  "

			# Notification that a test has ended.
			# TEST_END + testID + "," + testName
			TEST_END       = "%TESTE  "

			# Notification that a test had a error.
			# TEST_ERROR + testID + "," + testName.
			# After the notification follows the stack trace.
			TEST_ERROR     = "%ERROR  "

			# Notification that a test had a failure.
			# TEST_FAILED + testID + "," + testName.
			# After the notification follows the stack trace.
			TEST_FAILED    = "%FAILED "

			# Notification that a test trace has started.
			# The end of the trace is signaled by a TRACE_END
			# message. In between the TRACE_START and TRACE_END
			# the stack trace is submitted as multiple lines.
			TRACE_START    = "%TRACES "

			# Notification that a trace ends.
			TRACE_END      = "%TRACEE "

			# Notification that the expected result has started.
			# The end of the expected result is signaled by a EXPECTED_END.
			EXPECTED_START = "%EXPECTS"

			# Notification that an expected result ends.
			EXPECTED_END   = "%EXPECTE"

			# Notification that the actual result has started.
			# The end of the actual result is signaled by a ACTUAL_END.
			ACTUAL_START   = "%ACTUALS"

			# Notification that an actual result ends.
			ACTUAL_END     = "%ACTUALE"

			#Test identifier prefix for ignored tests.
			IGNORED_TEST_PREFIX = "@Ignore: "

		end # of MessageIds

		class SocketConnection
			def disconnect
				if @socket
					#debug "Closing socket"
					begin
						@socket.close
					rescue
						debug $!.to_s
					end
					@socket = nil
					#debug "Socket closed"
				end
			end

			def connectSocket(port)
				return false unless port > 0
				#debug "Opening socket on #{port}"
				for i in 1..10
					#debug "Iteration #{i}"
					begin 
						@socket = TCPSocket.new('localhost', port)
						#debug "Socket opened"
						return true
					rescue
						#debug $!.to_s
					end
					sleep 1
				end	
				false
			end			

			def sendMessage(message)
				#puts message
				if @socket
					@socket.puts message
				end
			end

			def notifyTestTreeEntry(testId, testName, hasChildren, testCount)
				sendMessage MessageIds::TEST_TREE + testId + ',' + escapeComma(testName) + ',' + hasChildren.to_s + ',' + testCount.to_s
			end

			def notifyTestStarted(testId, testName)
				sendMessage MessageIds::TEST_START + testId + "," + escapeComma(testName)
			end

			def notifyTestFailure(testId, testName, status)
				sendMessage status + testId + "," + escapeComma(testName)
			end

			def notifyTestEnded(testId, testName)
				sendMessage MessageIds::TEST_END + testId + "," + escapeComma(testName)
			end

			def notifyTestRunStarted(testCount)
				sendMessage MessageIds::TEST_RUN_START + testCount.to_s + " " + "v2"
			end

			def notifyTestRunEnded(elapsedTime)
				sendMessage MessageIds::TEST_RUN_END + elapsedTime.to_s
			end

			def escapeComma(s)
				s.gsub(/([\\,])/, '\\\\\1')
			end

			private :escapeComma

		end

		class DLTKFormatter < Spec::Runner::Formatter::BaseFormatter
			def initialize(*args)
				super
				@connection = SocketConnection.new()
			end

			def start(example_count)
				@connection.connectSocket ENV[EnvVars::PORT].to_i
				@connection.notifyTestRunStarted example_count
			end

			# tgrimm: Since rspec 1.2.4 add_example_group was renamed to example_groupe_started
			def add_example_group(example_group)
				example_group_started(example_group)
				if ::Spec::VERSION::MAJOR > 1 || ::Spec::VERSION::MINOR > 0
					super
				end
			end

			def example_group_started(example_group)
				options = @options ? @options : ::Spec::Runner.options
				examples_to_run = example_group.examples
				examples_to_run = examples_to_run.reject do |example|
					matcher = ::Spec::Example::ExampleMatcher.new(example_group.description.to_s, example.description)
					!matcher.matches?(options.examples)
				end unless options.examples.empty?
				if examples_to_run.size > 0 then
					# ssanders: Ensure that description is never blank
					description = example_group.description || example_group.to_s
					@connection.notifyTestTreeEntry getTestId(example_group), description, true, examples_to_run.size
					examples_to_run.each do |e|
						@connection.notifyTestTreeEntry getTestId(e), getTestName(e), false, 1
					end
				end
			end

			unless ::Spec::VERSION::MAJOR > 1 || ::Spec::VERSION::MINOR > 0
				def add_behaviour(description)
					add_example_group(description)
				end
			end

			def example_started(example)
				@connection.notifyTestStarted getTestId(example), getTestName(example)
			end

			def example_passed(example)
				@connection.notifyTestEnded getTestId(example), getTestName(example)
			end

			def example_pending(behaviour, example, message = nil)
				# ssanders: In older versions and for "pending" the example is actually passed as behaviour
				example = behaviour if example.is_a?(String)
				@connection.notifyTestEnded getTestId(example), MessageIds::IGNORED_TEST_PREFIX + getTestName(example)
			end

			EXPECTED_GOT_RE = /^expected: (.+),\n\s+got: (.+) \(using (==|===)\)$/s

			def example_failed(example, counter, failure)
				testId = getTestId(example)
				testName = getTestName(example)
				f = failure.exception
				if failure.expectation_not_met?
					@connection.notifyTestFailure testId, testName, MessageIds::TEST_FAILED
					if f.message =~ EXPECTED_GOT_RE
						@connection.sendMessage MessageIds::EXPECTED_START
						@connection.sendMessage $1
						@connection.sendMessage MessageIds::EXPECTED_END
						@connection.sendMessage MessageIds::ACTUAL_START
						@connection.sendMessage $2
						@connection.sendMessage MessageIds::ACTUAL_END
					end
				else
					@connection.notifyTestFailure testId, testName, MessageIds::TEST_ERROR
				end
				@connection.sendMessage MessageIds::TRACE_START
				@connection.sendMessage f.message
				f.backtrace.each { |line| @connection.sendMessage line }
				@connection.sendMessage MessageIds::TRACE_END
				@connection.notifyTestEnded testId, testName
			end

			def dump_summary(duration, example_count, failure_count, pending_count)
				@connection.notifyTestRunEnded((duration * 1000).to_i)
			end

			def close
				@connection.disconnect
				@connection = nil	
			end

			# internal

			def getTestId(example)
				return example.__id__.to_s
			end

			def getTestName(example)
				if example.respond_to?(:rspecTestName)
					name = example.rspecTestName
				elsif example.description
					name = example.description ? example.description : "NO NAME"
					if example.respond_to?(:location)
						name += "<" + example.location if example.location
					else
						name += "<" + example.backtrace if example.backtrace
					end
				end
				return name.to_s
			end

		end
	end
end

#if __FILE__ == $0
#	ARGV.push 'bowling_spec.rb'
#end

ARGV.push '--format'
ARGV.push 'DLTK::RSpec::DLTKFormatter'

# tgrimm: RSpec 1.2.1 was release 7 days after 1.2.0, so there's no real reason to support 1.2.0
raise "RSpec 1.2.0 is not supported, please update RSpec" if ::Spec::VERSION::STRING == '1.2.0'

if ::Spec::VERSION::MAJOR > 1 || ::Spec::VERSION::MINOR > 0
	if ::Spec.constants.include?("Extensions") && ::Spec::Extensions::Main.private_method_defined?(:rspec_options)
		options = rspec_options
	else # ssanders: RSpec > 1.1.4
		options = ::Spec::Runner.options
	end
	exit ::Spec::Runner::CommandLine.run(options)
else # ssanders: RSpec < 1.1
	exit ::Spec::Runner::CommandLine.run(ARGV, STDERR, STDOUT)
end
