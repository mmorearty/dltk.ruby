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
==== begin-while-modifier
i = 0
begin
print i
i += 1
end while i < 5
==
i = 0
begin
	print i
	i += 1
end while i < 5
==== begin-until-modifier
i = 0
begin
print i
i += 1
end until i > 3
==
i = 0
begin
	print i
	i += 1
end until i > 3
==== begin-until-modifier-multiline
i = 0
begin
print i
i += 1
end until
i > 3
==
i = 0
begin
	print i
	i += 1
end until
	i > 3
==== begin-rescue-any
begin
doStuff
rescue
print "Error!"
end
==
begin
	doStuff
rescue
	print "Error!"
end
==== begin-rescue
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
==== begin-rescue-multiple
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
==== begin-rescue-else
begin
doStuff
rescue SyntaxError
print "SyntaxError"
else
print "Other error"
end
==
begin
	doStuff
rescue SyntaxError
	print "SyntaxError"
else
	print "Other error"
end
==== begin-no-rescue-else
begin
doStuff
else
print "Other error"
end
==
begin
	doStuff
else
	print "Other error"
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
==== rescue-empty
begin

rescue

ensure

end
==
begin

rescue

ensure

end
==== begin-no-rescue-empty-else
	begin

		else

	end
==
begin

else

end
