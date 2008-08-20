require 'common/Logger'
require 'debugger/AbstractBreakpointManager'
require 'fast/FastLineBreakpoint'

module XoredDebugger
	class FastBreakpointManager < AbstractBreakpointManager
	    include Logger
        
        def initialize()
            super()
        end     
        
        def add_line_breakpoint_impl(id, file, line, temporary = false)
            rawBp = Debugger.add_breakpoint(file, line)
            FastLineBreakpoint.new(id, rawBp, temporary)
        end
    
        def remove_line_breakpoint_impl(bp)            
            rawBp = bp.raw_bp
            Debugger.remove_breakpoint(rawBp.id)
        end
        
        def check_line_breakpoint(raw_id)
            breakpoints.each() do |bp|
                if ((bp.is_a? FastLineBreakpoint) && (bp.raw_bp.id.equal?(raw_id)))                    
                    if (bp.temporary)
                        remove_breakpoint(bp.breakpoint_id)
                    end
                    return bp
                end
            end
            return nil
        end       
	end
end
