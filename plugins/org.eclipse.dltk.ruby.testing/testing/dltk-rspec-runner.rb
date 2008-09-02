require 'rubygems'
require 'socket'

gem 'rspec'

require 'spec/runner/formatter/base_formatter'

module DLTK
	module RSpec
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

		end # of MessageIds

		class DLTKFormatter < Spec::Runner::Formatter::BaseFormatter
			def start(example_count)
				puts "STARTED!!!"
			end

			def add_example_group(example_group)
				puts "[GROUP]"
				super
			end

			def example_started(example)
				puts "Started", example.inspect
			end

			def example_passed(example)
				puts "Passed", example.inspect
			end
		end
	end
end

if __FILE__ == $0
	ARGV.push 'bowling_spec.rb'
	ARGV.push '--format'
	ARGV.push 'DLTK::RSpec::DLTKFormatter'
end

load 'spec'
