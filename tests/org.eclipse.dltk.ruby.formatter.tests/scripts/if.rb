==== if
a = 1
if a == 1
puts "One"
end
==
a = 1
if a == 1
	puts "One"
end
==== if-else
a = 1
if a == 1
puts "One"
else
puts "Other"	
end
==
a = 1
if a == 1
	puts "One"
else
	puts "Other"	
end
==== if-elsif-else
a = 1
if a == 1  	
puts "One"
elsif a == 2 	
puts "Two" 
else
puts "Other"	
end
==
a = 1
if a == 1  	
	puts "One"
elsif a == 2 	
	puts "Two" 
else
	puts "Other"	
end
==== unless
a = 1
unless a == 1
puts "Other"
end
==
a = 1
unless a == 1
	puts "Other"
end
==== unless+else
a = 1
unless a == 1
puts "Other"
else
puts "One"
end
==
a = 1
unless a == 1
	puts "Other"
else
	puts "One"
end
==== if-clause-as-argument-not-shifted
puts( if objects.length == 1
YAML::dump( *objects )
else
YAML::dump_stream( *objects )
end )
==
puts( if objects.length == 1
	YAML::dump( *objects )
else
	YAML::dump_stream( *objects )
end )
==== empty-if-empty-else
if true

else

end
==
if true

else

end
==== if-else-empty
if z ==1
print z    
else

end
==
if z ==1
	print z    
else

end
==== if-empty-else
if z ==1

else
print z
end
==
if z ==1

else
	print z
end
==== if-modifier
raise "Please, use ruby 1.5.4 or later." if RUBY_VERSION < "1.5.4"
==
raise "Please, use ruby 1.5.4 or later." if RUBY_VERSION < "1.5.4"
