require 'rdoc/ri/ri_driver'

ENV["RI"] = "-f html -T"

endMarker = "DLTKDOCEND"
ri = RiDriver.new

while true do
	s = STDIN.gets
	if (s.nil?) 
	  break;
	end
	s = s.chop!
	begin
		ri.get_info_for(s)
		STDOUT.puts "\n" + endMarker
		STDOUT.flush
	rescue RiError => e
		STDOUT.puts "#{e}"
		STDOUT.puts "\n" + endMarker
		STDOUT.flush
		next
	end
end
