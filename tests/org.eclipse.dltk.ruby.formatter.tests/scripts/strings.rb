==== multiline-string
a = "string
string"
==
a = "string
string"
==== multiline-string-method
def z(arg)
print "string
string\n"
end
==
def z(arg)
	print "string
string\n"
end
==== multiline-string-class
class Z
s = "a
b"
def m
print "m
m"
end    
end
==
class Z
	s = "a
b"
	def m
		print "m
m"
	end    
end
==== multiline-string-if-block
a = 1
if a==1
puts "string
printed"
end
==
a = 1
if a==1
	puts "string
printed"
end
==== multiline-string-while-block
while a>0
puts "string
printed"
end
==
while a>0
	puts "string
printed"
end
==== multiline-string-while-modifier
i = 0 
begin
print "aaa
bbb"
i +=1
end while i < 5
==
i = 0 
begin
	print "aaa
bbb"
	i +=1
end while i < 5
==== multiline-string-rescue-block
begin
doStuff
rescue SyntaxError
print "Error!
Error!"
end
==
begin
	doStuff
rescue SyntaxError
	print "Error!
Error!"
end
==== multiline-string-do-block
[ 1, 2, 3 ].each do |x|
print "xxx
xxx"
end
==
[ 1, 2, 3 ].each do |x|
	print "xxx
xxx"
end
==== multiline-string-spaces
def method
print "Hello
 "
puts "world"
end
method
==
def method
	print "Hello
 "
	puts "world"
end
method
