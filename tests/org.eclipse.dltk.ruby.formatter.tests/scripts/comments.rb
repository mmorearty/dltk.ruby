==== method-comments
def a
#comment
end
==
def a
	#comment
end
==== class-comments
class Hello

#method 1
def method1
			#inside method 1
return 1
end
	
#method 2
def method2
#inside method 2
return 2
end

end
==
class Hello

	#method 1
	def method1
		#inside method 1
		return 1
	end
	
	#method 2
	def method2
		#inside method 2
		return 2
	end

end
==== begin-comments
begin
doStuff
#zz
rescue
#error
print "Error!"
end
==
begin
	doStuff
	#zz
rescue
	#error
	print "Error!"
end
==== begin-comments-2
begin
		#c1
doStuff
rescue
#c2
puts "Failed!"
ensure
#c3
tidyUp
end
==
begin
	#c1
	doStuff
rescue
	#c2
	puts "Failed!"
ensure
	#c3
	tidyUp
end
==== until-comments
a = 1
until a >= 10:
#increment
a += 1
end
puts a
==
a = 1
until a >= 10:
	#increment
	a += 1
end
puts a
==== while-comments
a = 1
while a < 10
#increment
a += 1
end
puts a
==
a = 1
while a < 10
	#increment
	a += 1
end
puts a
==== for-comments
for i in 1..10
#print
puts i
end
==
for i in 1..10
	#print
	puts i
end
==== case-comments
case age
when 0
#zero
puts "zero"
when 1
#one
puts "one"
when 2
#two
puts "two"
else
#other
puts "other"
end
==
case age
	when 0
		#zero
		puts "zero"
	when 1
		#one
		puts "one"
	when 2
		#two
		puts "two"
	else
		#other
		puts "other"
end
==== simple-do-comments
num = 1
7.times do
#print
print num
num += 1
end
==
num = 1
7.times do
	#print
	print num
	num += 1
end
==== if-comments
a = 1
if a == 1
#one  	
puts "One"
elsif a == 2
#two 	
puts "Two" 
else
#other
puts "Other"	
end
==
a = 1
if a == 1
	#one  	
	puts "One"
elsif a == 2
	#two 	
	puts "Two" 
else
	#other
	puts "Other"	
end
==== unless-comments
a = 1
unless a == 1
#other
puts "Other"
else
#one
puts "One"
end
==
a = 1
unless a == 1
	#other
	puts "Other"
else
	#one
	puts "One"
end
==== modules-comments
#module
module Some
#module
module Thing
VERSION = 1
#class
class Hello
#method
def execute
#inside method
puts "Hello, world"
end

end

end
end
==
#module
module Some
	#module
	module Thing
		VERSION = 1
		#class
		class Hello
			#method
			def execute
				#inside method
				puts "Hello, world"
			end

		end

	end
end
