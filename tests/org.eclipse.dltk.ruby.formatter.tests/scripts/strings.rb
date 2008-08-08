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
==== multiline-string-while-block-condition
while x != "A
BBB"
puts x
end
==
while x != "A
BBB"
	puts x
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
==== dstring
[1,2,3].each do |x|
puts "Iteration
#{x}"
end
==
[1,2,3].each do |x|
	puts "Iteration
#{x}"
end
==== xstring
[1,2,3].each do |x|
puts `ls
date`
end
==
[1,2,3].each do |x|
	puts `ls
date`
end
==== multiline-string-until
i = "a"
until i == "a\
zz"
print i
i += "z"
end
==
i = "a"
until i == "a\
zz"
	print i
	i += "z"
end
==== multiline-string-until-modifier
i = "a"
begin
print i
i += "z"
end until i == "a\
zz"
==
i = "a"
begin
	print i
	i += "z"
end until i == "a\
zz"
==== multiline=string-percent
def z
a = %!ola\
lala!
print a
end
==
def z
	a = %!ola\
lala!
	print a
end
==== string-as-return
def xName
"AAAA"
end
puts xName
==
def xName
	"AAAA"
end
puts xName
==== string-as-args
dump(['all'], *index, &block)
==
dump(['all'], *index, &block)
