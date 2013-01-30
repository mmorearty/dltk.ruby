require 'socket'
require 'monitor'
require 'common/Params'
require 'common/Logger'

module XoredDebugger
	class Communicator
	    include Logger
        
	    def initialize(thread)
	        params = Params.instance
            @socket = TCPSocket.new(params.host, params.port)
            @socket.sync = true
            @monitor = Monitor.new()
	    end
        
	    def receive_command
            #IDE: command [SPACE] [args] -- data [NULL]
            line = ''
            # Ruby 1.8 uses numbers as characters and Ruby 1.9 uses single
            # character strings. Select the terminator based on version.
            terminator = RUBY_VERSION.start_with?("1.8") ? 0 : "\0"
            while((ch = @socket.getc) != terminator)
                if (ch.nil?)
                    raise IOError
                end
                line << ch
            end
            log('RECEIVED: ' + line)
            return Command.new(line)
        end
        
        def sendPacket(packet)
            @monitor.synchronize do
	            #DEBUGGER: [NUMBER] [NULL] XML(data) [NULL]
	            xml = packet.to_xml
	            log('SENDING: ' + xml)            
	            @socket.write(xml.length.to_s)
	            @socket.putc(0)
	            @socket.write(xml)
	            @socket.putc(0)
	            @socket.flush
            end
        end	  
        
        def close()
            begin
                @socket.close
            rescue Exception
            end
        end  
	end 
end
