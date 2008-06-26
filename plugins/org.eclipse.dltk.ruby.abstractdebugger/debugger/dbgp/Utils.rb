# TODO: Perform more smart conversions 
require 'cgi'
require 'common/Params'  
require 'dbgp/InitPacket'
require 'dbgp/ErrorElement'
require 'dbgp/Communicator'

module XoredDebuggerUtils
    #
    # uri -> path
    #
    def uri_to_path(uri)
        # File.expand_path      
        CGI.unescape(uri).sub('file:///', '')
    end

    if RUBY_PLATFORM =~ /mswin/
		PATH_SPLIT_RE = /[\/\\]/
	else
		PATH_SPLIT_RE = /\//
	end

    #
    # path -> uri
    #
    def path_to_uri(path)
      #puts 'path_to_uri ' + path
      result = 'file://'
      segments = path.split PATH_SPLIT_RE
      index = 0
      for segment in segments
        #puts 'segment' + index.to_s + '=[' + segment + ']'
        if (segment == '' && index == 0)
          next
        end
        segment = escapeSegment(segment)
        result += '/'
        result += segment
        #puts result
        ++index
      end
      return result
    end

    def escapeSegment(string) 
      string.gsub(/([^a-zA-Z0-9_.:-]+)/n) do
        '%' + $1.unpack('H2' * $1.size).join('%').upcase
      end
    end
    
    def normalize_path(path)
        Pathname.new(path).expand_path.to_s
    end
    
    #
    # Thread label
    #
    def get_thread_label(thread)
        main_label = (thread == Thread.main ? ' (main)' : '' )
        label = sprintf("Thread%s id=%d", main_label, thread.object_id)
        
# Can cause null pointer on JRuby
#        priority = thread.priority
#        if priority 
#            label = sprintf("%s, priority=%d", label, priority)
#        end
        label
    end     
    
    # Helper method to remove debugger stack frame from child thread
    def get_stack_depth(context)
        if (context.thread != Thread.main)
            # Hide debugger's frame
            context.stack_frames_num - 1
        else
            context.stack_frames_num
        end
    end      
end

module XoredDebugger       
	class InitError
	    include XoredDebuggerUtils
        
	    def initialize(code)        
		    params = Params.instance
	        communicator = Communicator.new(Thread.current)
	        packet = InitPacket.new(params.key, get_thread_label(Thread.current), params.script)
            packet.set_data(ErrorElement.new(code))
		    communicator.sendPacket(packet)
	
	        # wait until connection closed
	        begin
	            communicator.receiveCommand
	        rescue Exception
	        ensure
	            communicator.close                          
	        end
	    end
	end      
end
