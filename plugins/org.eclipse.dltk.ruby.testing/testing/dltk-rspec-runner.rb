require 'rubygems'
require 'socket'

gem 'rspec'

require 'spec'
require 'spec/runner/formatter/base_formatter'

module Spec
	module Example
		module ExampleMethods
			def implementationProc
				@_implementation
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
					notifyTestRunEnded ((Time.now.to_f - @startTime.to_f) * 1000).to_i
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
			def initialize(options, where)
				super
				@connection = SocketConnection.new()
			end

			def start(example_count)
				@connection.connectSocket ENV[DLTK::RSpec::EnvVars::PORT].to_i
				@connection.notifyTestRunStarted 0
			end

			def add_example_group(example_group)
				#puts "[GROUP]"
				#puts example_group.description_text
				#puts example_group.spec_path
				examples = example_group.examples
				@connection.notifyTestTreeEntry getTestId(example_group), example_group.description_text, true, examples.size
				examples.each do |e|
					@connection.notifyTestTreeEntry getTestId(e), getTestName(e), false, 1
				end
				super
			end

			def example_started(example)
				#puts "Started", example.inspect
				@connection.notifyTestStarted getTestId(example), getTestName(example)
				#example.cl
				#puts example.implementationProc
				#puts example.description
				#puts example.implementation_backtrace
			end

			def example_passed(example)
				@connection.notifyTestEnded getTestId(example), getTestName(example)
				#puts "Passed", example.inspect
			end

			def example_pending(example, message)
				@connection.notifyTestEnded getTestId(example), MessageIds::IGNORED_TEST_PREFIX + getTestName(example)
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
				return example.inspect				
			end

		end
	end
end

if __FILE__ == $0
	ARGV.push 'bowling_spec.rb'
	ARGV.push '--format'
	ARGV.push 'DLTK::RSpec::DLTKFormatter'
end

exit ::Spec::Runner::CommandLine.run(rspec_options)
