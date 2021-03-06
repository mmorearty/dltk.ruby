require 'test/unit'
require 'common/Logger'
require 'dbgp/CommandHandler.rb'
require 'dbgp/ThreadManager'
require 'test/MockDebugger'

require 'test/FakeParams'
require 'test/SimpleServer'

module XoredDebugger    
    class CommandHandlerTest < Test::Unit::TestCase
        include Logger
                
        def setup
            log('---------------------------- STARTUP!!! -------------------------------')
            SimpleServer.start()
            @debugger = MockDebugger.new()
            @thread_manager = ThreadManager.new(@debugger)
            @breakpoint_manager = @debugger.breakpoint_manager
            @context = @debugger.current_context
            @handler = CommandHandler.new(@thread_manager, Thread.current)
            @capture_manager = @thread_manager.capture_manager
        end
        
        def teardown
            @debugger = nil
            @breakpoint_manager = nil
            @context = nil
            @handler = nil
            @capture_manager = nil

            @thread_manager.terminate if @thread_manager
            @thread_manager = nil
            log('Thread manager terminated')
            @debugger.terminate if @debugger
            @debugger = nil
            log('Debugger terminated')
            SimpleServer.stop()
            log('Server terminated')
            assert(!SimpleServer.started?) 
            log('---------------------------- TEAR DOWN!!! -------------------------------')
        end
        
        # Status
        def test_handle_status
            @context.status = 'starting'             
            
            response = @handler.handle(Command.new('status -i 5'))
            assert_equal('starting', response.get_attribute('status'))
            assert_equal('status', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('ok', response.get_attribute('reason'))
            
                       
            @context.status = 'running'             

            response = @handler.handle(Command.new('status -i 5'))
            assert_equal('running', response.get_attribute('status'))
            assert_equal('status', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('ok', response.get_attribute('reason'))
        end
        
        # Options and Configuration
        def test_handle_feature_get
            response = @handler.handle(Command.new('feature_get -i 5 -n protocol_version'))
            assert_equal('feature_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('protocol_version', response.get_attribute('feature_name'))
            assert_equal('1', response.get_attribute('supported'))
            assert_equal('1', response.get_data)

            response = @handler.handle(Command.new('feature_get -i 5'))
            assert_equal(3, response.get_error)
                
            response = @handler.handle(Command.new('feature_get -i 5 -n not_supported_option'))
            assert_equal('feature_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('not_supported_option', response.get_attribute('feature_name'))
            assert_equal('0', response.get_attribute('supported'))
        end
        
        def test_handle_feature_set
            response = @handler.handle(Command.new('feature_set -i 5 -n protocol_version -v 2.0'))
            assert_equal('feature_set', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('protocol_version', response.get_attribute('feature_name'))
            assert_equal('0', response.get_attribute('success'))

            response = @handler.handle(Command.new('feature_set -i 5'))
            assert_equal(3, response.get_error)
                
            response = @handler.handle(Command.new('feature_set -i 5 -n max_depth -v 10'))
            assert_equal('feature_set', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('max_depth', response.get_attribute('feature_name'))
            assert_equal('1', response.get_attribute('success'))
        end
        
        
        # Continuation Commands
        def test_handle_run
            @context.status = 'break'                         
            response = @handler.handle(Command.new('run -i 5'))
            assert_equal(nil, response)
            assert_equal('running', @context.status)
            
            response = @handler.handle(Command.new('run -i 5'))
            assert_not_equal(nil, response)
            assert_equal(5, response.get_error)            
        end
        
        def test_handle_step_into
            @context.status = 'break'                         
            response = @handler.handle(Command.new('step_into -i 5'))
            assert_equal(nil, response)
            assert_equal('break', @context.status)
            
            @context.status = 'running'                         
            response = @handler.handle(Command.new('step_into -i 5'))
            assert_not_equal(nil, response)
            assert_equal(5, response.get_error)            
        end
        
        def test_handle_step_over
            @context.status = 'break'                         
            response = @handler.handle(Command.new('step_over -i 5'))
            assert_equal(nil, response)
            assert_equal('break', @context.status)
            
            @context.status = 'running'                         
            response = @handler.handle(Command.new('step_over -i 5'))
            assert_not_equal(nil, response)
            assert_equal(5, response.get_error)            
        end
        
        def test_handle_step_out
            @context.status = 'running'                         
            response = @handler.handle(Command.new('step_out -i 5'))
            assert_not_equal(nil, response)
            assert_equal(5, response.get_error)                    

            @context.status = 'break'                         
            response = @handler.handle(Command.new('step_out -i 5'))
            assert_equal(nil, response)
            assert_equal('stopped', @context.status)            
        end
		
        def test_handle_stop
            @context.status = 'running'                         
            response = @handler.handle(Command.new('stop -i 5'))
            assert_equal('stop', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('ok', response.get_attribute('reason'))
            assert_equal('stopped', response.get_attribute('status'))
            
            assert_equal('stopped', @context.status)            

            @context.status = 'break'                         
            response = @handler.handle(Command.new('stop -i 5'))
            assert_equal('stop', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('ok', response.get_attribute('reason'))
            assert_equal('stopped', response.get_attribute('status'))

            assert_equal('stopped', @context.status)            
        end
        
        def test_handle_detach
            response = @handler.handle(Command.new("detach -i 5"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end
        
        
        #breakpoints
        def test_handle_breakpoint_set
            response = @handler.handle(Command.new("breakpoint_set -i 5 -t line -f test.rb -n 100"))            
            assert_equal(0, response.get_error)
            assert(!response.get_attribute('id').nil?)

            response = @handler.handle(Command.new("breakpoint_set -i 5 -t exception -x Exception"))            
            assert_equal(0, response.get_error)
            assert(!response.get_attribute('id').nil?)

            response = @handler.handle(Command.new("breakpoint_set -i 5 -t exception -x Fixnum"))            
            assert_equal(200, response.get_error)
                        
            response = @handler.handle(Command.new("breakpoint_set -i 5"))            
            assert_equal(3, response.get_error)

            response = @handler.handle(Command.new("breakpoint_set -i 5 -t unknownType"))            
            assert_equal(201, response.get_error)
        end
        
        def test_handle_breakpoint_get
            bp = @breakpoint_manager.add_line_breakpoint('test.rb', 100)
            response = @handler.handle(Command.new("breakpoint_get -i 5 -d " + bp.breakpoint_id.to_s))
            expected = BreakpointElement.new(bp).to_xml
            assert_equal(expected, response.get_data.to_s) 
            assert_equal('breakpoint_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
                        
            bp = @breakpoint_manager.add_exception_breakpoint('Exception')
            response = @handler.handle(Command.new("breakpoint_get -i 5 -d " + bp.breakpoint_id.to_s))
            expected = BreakpointElement.new(bp).to_xml
            assert_equal(expected, response.get_data.to_s) 
            assert_equal('breakpoint_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
                
            response = @handler.handle(Command.new("breakpoint_get -i 5 -d 97575795"))
            assert_equal(205, response.get_error)            
        end
        
        def test_handle_breakpoint_update
            bp = @breakpoint_manager.add_line_breakpoint('test.rb', 100)
            
            response = @handler.handle(Command.new("breakpoint_update -i 5 -d " + bp.breakpoint_id.to_s))
            assert_equal(3, response.get_error)    

            response = @handler.handle(Command.new("breakpoint_update -i 5 -n 101 -d " + bp.breakpoint_id.to_s))
            assert_equal('breakpoint_update', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal(101, bp.lineno)
            
            response = @handler.handle(Command.new("breakpoint_update -i 5 -d 868986086"))
            assert_equal(205, response.get_error)    
        end
        
        def test_handle_breakpoint_remove
            bp = @breakpoint_manager.add_line_breakpoint('test.rb', 100)
            response = @handler.handle(Command.new("breakpoint_remove -i 5 -d " + bp.breakpoint_id.to_s))
            assert_equal('breakpoint_remove', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))

            response = @handler.handle(Command.new("breakpoint_remove -i 5 -d " + bp.breakpoint_id.to_s))
            assert_equal(205, response.get_error)    
        end
        
        def test_handle_breakpoint_list
            @breakpoint_manager.clear
            response = @handler.handle(Command.new("breakpoint_list -i 5"))
            assert_equal(nil, response.get_data) 

            line = @breakpoint_manager.add_line_breakpoint('test.rb', 100)
            expt = @breakpoint_manager.add_exception_breakpoint('Exception')
            expected = BreakpointElement.new(line).to_xml + BreakpointElement.new(expt).to_xml
            response = @handler.handle(Command.new("breakpoint_list -i 5"))
            assert_equal(expected, response.get_data) 
        end
        
        
        # Stack commands
        def test_handle_stack_depth
            @context.status = 'break'            
            response = @handler.handle(Command.new('stack_depth -i 5'))
            assert_equal(@context.stack_frames_num.to_s, response.get_attribute('depth'))
            
            @context.status = 'running'            
            response = @handler.handle(Command.new('stack_depth -i 5'))
            assert_equal(5, response.get_error)            
        end
        
        def test_handle_stack_get
            @context.status = 'running'            
            response = @handler.handle(Command.new('stack_get -i 5'))
            assert_equal(5, response.get_error)            
            
            @context.status = 'break'            
            response = @handler.handle(Command.new('stack_get -i 5 -d 0'))
            expected = StackLevelElement.new(0, @context.stack_frame(0)).to_xml
            assert_equal(expected, response.get_data.to_s) 
            assert_equal('stack_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            
            response = @handler.handle(Command.new('stack_get -i 5'))
            expected = StackLevelElement.new(0, @context.stack_frame(0)).to_xml + StackLevelElement.new(1,@context.stack_frame(1)).to_xml 
            assert_equal(expected, response.get_data.to_s) 
            assert_equal('stack_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
        end
        
        
        # Context commands
        def test_handle_context_names
            response = @handler.handle(Command.new('context_names -i 5'))
            assert_equal("<response command=\"context_names\" transaction_id=\"5\">" +
                "<context name=\"Local\" id=\"0\"/><context name=\"Global\" id=\"1\"/>" + 
                "<context name=\"Class\" id=\"2\"/></response>", response.to_xml)
        end
        
        def test_handle_context_get
            @context.status = 'running'                         
            response = @handler.handle(Command.new('context_get -i 5'))
            assert_equal(5, response.get_error)                    

            @context.status = 'break'                         
            response = @handler.handle(Command.new('context_get -i 5'))
            assert_equal(0, response.get_error)                    
            assert_equal('0', response.get_attribute('context'))                    
           
            response = @handler.handle(Command.new('context_get -i 5 -c 1'))
            assert_equal(0, response.get_error)                    
            assert_equal('1', response.get_attribute('context'))                    
        end
        
        
        # Common Data Types
        def test_handle_typemap_get
            @context.status = 'break'                         
            response = @handler.handle(Command.new('typemap_get -i 5'))
            assert_equal('typemap_get', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('http://www.w3.org/2001/XMLSchema', response.get_attribute('xmlns:xsd'))
            assert_equal('http://www.w3.org/2001/XMLSchema-instance', response.get_attribute('xmlns:xsi'))
        end
        
        
        # Properties, variables and values
        def test_handle_property_get
            @context.status = 'running'                         
            response = @handler.handle(Command.new("property_get -i 5 -n 2+2"))
            assert_equal(5, response.get_error)                    
            
            @context.status = 'break'                         
            expected = PropertyElement.new(Kernel.eval('2+2'), '2+2')
            response = @handler.handle(Command.new("property_get -i 5 -n 2+2"))
            assert_equal(expected.to_xml, response.get_data.to_s)            
            
            response = @handler.handle(Command.new("property_get -i 5 -n wefipiwpif"))
            assert_equal(300, response.get_error)                  
        end
        
        def test_handle_property_set
            @context.status = 'running'                         
            response = @handler.handle(Command.new("property_set -i 5 -n $stdout.sync -- " + Base64.encode64('true')))
            assert_equal(5, response.get_error)                    

            @context.status = 'break'
            $stdout.sync = false                         
            response = @handler.handle(Command.new("property_set -i 5 -n $stdout.sync -- " + Base64.encode64('true')))
            assert_equal('1', response.get_attribute('success'))
            assert_equal(true, $stdout.sync)            
            
            response = @handler.handle(Command.new("property_set -i 5 -n wefipiwpif -- " + Base64.encode64('value')))
            assert_equal(300, response.get_error)                  
            assert_equal('0', response.get_attribute('success'))
        end
        
        def test_handle_property_value
            @context.status = 'running'                         
            response = @handler.handle(Command.new("property_value -i 5 -n 2+2"))
            assert_equal(5, response.get_error)                    
            
            @context.status = 'break'                         
            response = @handler.handle(Command.new("property_value -i 5 -n 2+2"))
            assert_equal('4', response.get_data.to_s)            
            
            response = @handler.handle(Command.new("property_get -i 5 -n wefipiwpif"))
            assert_equal(300, response.get_error)                  
        end
                
        # Source
        def test_handle_source
            response = @handler.handle(Command.new('source -i 5 -z thisFileWillNotBeFound'))
            assert_equal(3, response.get_error)

            response = @handler.handle(Command.new('source -i 5 -f thisFileWillNotBeFound'))
            assert_equal(100, response.get_error)
            
            response = @handler.handle(Command.new('source -i 5 -f CommandHandlerTest.rb'))
            assert_equal(0, response.get_error)
            assert_not_equal(nil, response.get_data)
                        
        end
        
        
        # Stdout, stderr redirection
        def test_handle_stdout
            capturer = @capture_manager.stdout_capturer
            capturer.state = CaptureManager::REDIRECT

            response = @handler.handle(Command.new('stdout -i 5 -c 0'))
            assert_equal('1', response.get_attribute('success'))
            assert_equal(CaptureManager::DISABLE, capturer.state)
        end
        
        def test_handle_stderr
            capturer = @capture_manager.stderr_capturer
            capturer.state = CaptureManager::REDIRECT

            response = @handler.handle(Command.new('stderr -i 5 -c 0'))
            assert_equal('1', response.get_attribute('success'))
            assert_equal(CaptureManager::DISABLE, capturer.state)
        end
        
        
        ######################################
        # Extended Commands
        ######################################
        
        def test_handle_stdin
            response = @handler.handle(Command.new('stdin -i 5 -c 1'))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

	
        def test_handle_break
            @context.status = 'running'                         
            response = @handler.handle(Command.new('break -i 5'))
            assert_equal('break', response.get_attribute('command'))
            assert_equal('5', response.get_attribute('transaction_id'))
            assert_equal('ok', response.get_attribute('reason'))
            assert_equal('break', response.get_attribute('status'))
            assert_equal(AbstractContext::BREAK, @context.status)
            
            response = @handler.handle(Command.new('break -i 5'))
            assert_equal(5, response.get_error)                    
        end

        def test_handle_eval
            @context.status = 'running'                         
            response = @handler.handle(Command.new("eval -i 5 -- " + Base64.encode64('2+2')))
            assert_equal(5, response.get_error)                    
            
            @context.status = 'break'            
            response = @handler.handle(Command.new("eval -i 5 -- " + Base64.encode64('loop { sleep 1 }')))
            assert_equal(206, response.get_error)                  
            assert_equal('0', response.get_attribute('success'))

            
            response = @handler.handle(Command.new("eval -i 5 -- " + Base64.encode64('+!fpefnpwen')))
            assert_equal(206, response.get_error)                  
            assert_equal('0', response.get_attribute('success'))                              

            expected = PropertyElement.new(Kernel.eval('2+2'), '2+2')
            response = @handler.handle(Command.new("eval -i 5 -- " + Base64.encode64('2+2')))
            assert_equal(expected.to_xml, response.get_data.to_s)            
            assert_equal('1', response.get_attribute('success'))                  
        end

        def test_handle_expr
            response = @handler.handle(Command.new("expr -i 5 -- SGVsbG8=\n"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        def test_handle_exec
            response = @handler.handle(Command.new("exec -i 5 -- SGVsbG8=\n"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        # Spawnpoint commands
        def test_handle_spawnpoint_set
            response = @handler.handle(Command.new("spawnpoint_set -i 5"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        def test_handle_spawnpoint_get
            response = @handler.handle(Command.new("spawnpoint_get -i 5 -d 1"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        def test_handle_spawnpoint_update
            response = @handler.handle(Command.new("spawnpoint_update -i 5 -d 1"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        def test_handle_spawnpoint_remove
            response = @handler.handle(Command.new("spawnpoint_remove -i 5 -d 1"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        def test_handle_spawnpoint_list
            response = @handler.handle(Command.new("spawnpoint_list -i 5"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end

        # interact - Interactive Shell
        def test_handle_interact
            response = @handler.handle(Command.new("interact -i 5 -m mode -- base64(code)"))
            assert_equal(4, response.get_error, 'Please provide unit test for implementation')
        end
    end
end
