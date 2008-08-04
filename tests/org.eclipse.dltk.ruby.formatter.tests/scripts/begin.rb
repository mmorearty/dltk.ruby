==== begin-empty
begin

end
==
begin

end
==== begin-just-code
begin
puts "Hello"
end
==
begin
	puts "Hello"
end
==== rescue
begin
doStuff
rescue SyntaxError
print "Error!"
end
==
begin
	doStuff
rescue SyntaxError
	print "Error!"
end
==== rescue-multiple
	begin
doStuff
	rescue SyntaxError
print "Syntax error!"
rescue IOError
print "IO error!"
end
==
begin
	doStuff
rescue SyntaxError
	print "Syntax error!"
rescue IOError
	print "IO error!"
end
==== rescue-ensure
begin
doStuff
rescue
puts "Failed!"
ensure
tidyUp
end
==
begin
	doStuff
rescue
	puts "Failed!"
ensure
	tidyUp
end
==== rescue-else-ensure
begin
doStuff
rescue
puts "Failed!"
else
puts "Succeeded"
ensure
tidyUp
end
==
begin
	doStuff
rescue
	puts "Failed!"
else
	puts "Succeeded"
ensure
	tidyUp
end
