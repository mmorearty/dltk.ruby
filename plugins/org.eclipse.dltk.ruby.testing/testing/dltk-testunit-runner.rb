require 'test/unit/ui/console/testrunner'
require 'socket'

module DLTK
	module TestUnit
		module EnvVars
			# environment variable name to pass communication port number
			# to the launched script
			PORT = "RUBY_TESTING_PORT"
			PATH = "RUBY_TESTING_PATH"
		end

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

		end

		class Runner < Test::Unit::UI::Console::TestRunner
			def start
				connectSocket ENV[EnvVars::PORT].to_i
				@testsByName = {}
				super
			ensure
				disconnect(nil)
			end

			DLTK = "DLTK/#{__id__}"

			def attach_to_mediator
				super
				@mediator.add_listener(Test::Unit::TestResult::FAULT, &method(:onTestFailure))
				@mediator.add_listener(Test::Unit::UI::TestRunnerMediator::STARTED, DLTK, &method(:connect))
				@mediator.add_listener(Test::Unit::UI::TestRunnerMediator::FINISHED, DLTK, &method(:disconnect))
				@mediator.add_listener(Test::Unit::TestCase::STARTED, DLTK, &method(:onTestStarted))
				@mediator.add_listener(Test::Unit::TestCase::FINISHED, DLTK, &method(:onTestFinished))
			end

			def debug(msg)
				puts "[DEBUG] #{msg}"
			end

			def connect(result)
				@startTime = Time.now
				notifyTestRunStarted @suite.size
				sendTree(@suite)
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

			def disconnect(result)
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

			def onTestStarted(name)
				t = @testsByName[name]
				if t
					notifyTestStarted getTestId(t), t.name
				else
					#debug "warn: test #{name} not found"
					notifyTestStarted name, name
				end
			end

			def onTestFinished(name)
				t = @testsByName[name]
				if t
					notifyTestEnded getTestId(t), t.name
				else
					#debug "warn: test #{name} not found"
					notifyTestEnded name, name  
				end
			end

			ACTUAL_EXPECTED_RE = /^<(.+)> expected but was\n<(.+)>.$/s

			def onTestFailure(f)
				if f.is_a? Test::Unit::Failure
					t = @testsByName[f.test_name]
					if t
						notifyTestFailure getTestId(t), t.name, MessageIds::TEST_FAILED
					else
						notifyTestFailure f.test_name, f.test_name, MessageIds::TEST_FAILED
					end
					if f.message =~ ACTUAL_EXPECTED_RE
						sendMessage MessageIds::EXPECTED_START
						sendMessage $1
						sendMessage MessageIds::EXPECTED_END
						sendMessage MessageIds::ACTUAL_START
						sendMessage $2
						sendMessage MessageIds::ACTUAL_END
					end
					sendMessage MessageIds::TRACE_START
					sendMessage f.message
					f.location.each { |line| sendMessage line }
					sendMessage MessageIds::TRACE_END
				elsif f.is_a? Test::Unit::Error
					t = @testsByName[f.test_name]
					if t
						notifyTestFailure getTestId(t), t.name, MessageIds::TEST_ERROR
					else
						notifyTestFailure f.test_name, f.test_name, MessageIds::TEST_ERROR
					end
					sendMessage MessageIds::TRACE_START
					e = f.exception
					sendMessage e.message
					e.backtrace.each { |line| sendMessage line }					
					sendMessage MessageIds::TRACE_END
				end
			end

			def notifyTestRunStarted(testCount)
				sendMessage MessageIds::TEST_RUN_START + testCount.to_s + " " + "v2"
			end

			def notifyTestRunEnded(elapsedTime)
				sendMessage MessageIds::TEST_RUN_END + elapsedTime.to_s
			end

			def sendTree(suite)
				suite.tests.each do |t|
					@testsByName[t.name] = t
					if t.is_a? Test::Unit::TestSuite
						notifyTestTreeEntry getTestId(t), t.name, true, t.size
						sendTree t
					else
						notifyTestTreeEntry getTestId(t), t.name, false, 1
					end
				end
			end

			def notifyTestTreeEntry(testId, testName, hasChildren, testCount)
				sendMessage MessageIds::TEST_TREE + testId + ',' + escapeComma(testName) + ',' + hasChildren.to_s + ',' + testCount.to_s
			end

			def notifyTestStarted(testId, testName)
				sendMessage MessageIds::TEST_START + testId + "," + testName
			end

			def notifyTestEnded(testId, testName)
				sendMessage MessageIds::TEST_END + testId + "," + testName
			end

			def notifyTestFailure(testId, testName, status)
				sendMessage status + testId + "," + testName
			end

			def getTestId(t)
				return t.__id__.to_s
			end

			def escapeComma(s)
				s.gsub('[\\,]', '\\\1')
			end

			def sendMessage(message)
				#debug message
				if @socket
					@socket.puts message
				end
			end

		end
	end
end

at_exit do
	unless $! || Test::Unit.run?
		port = ENV[DLTK::TestUnit::EnvVars::PORT].to_i
		if port != 0
			path = ENV[DLTK::TestUnit::EnvVars::PATH]
			autoRunner = Test::Unit::AutoRunner.new(path != nil)
			autoRunner.output_level = Test::Unit::UI::SILENT
			autoRunner.base = path if path
			autoRunner.runner = proc do |r|
				DLTK::TestUnit::Runner
			end
			exit autoRunner.run 
		end
	end
end

if __FILE__ == $0
	#debug mode
	#require 'test_math'
	#require 'test_shoulda'
end
