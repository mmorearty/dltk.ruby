==== while
a = 1
while a < 10
a += 1
end
puts a
==
a = 1
while a < 10
	a += 1
end
puts a
==== while modifier
		a = 1
	a += 1 while a < 10
puts a
==
a = 1
a += 1 while a < 10
puts a
==== while-modifier-complex
		begin
i += 1
		end while i < str_len and @str[i].chr == "\0"
==
begin
	i += 1
end while i < str_len and @str[i].chr == "\0"
==== while-empty
while a < 10

end
==
while a < 10

end
==== for
for i in 1..10
puts i
end
==
for i in 1..10
	puts i
end
==== for-empty
for i in 1..10
end
==
for i in 1..10
end
==== until
a = 1
until a >= 10
a += 1
end
puts a
==
a = 1
until a >= 10
	a += 1
end
puts a
==== until:
a = 1
until a >= 10:
a += 1
end
puts a
==
a = 1
until a >= 10:
	a += 1
end
puts a
==== until-do
a = 1
until a >= 10 do
a += 1
end
puts a
==
a = 1
until a >= 10 do
	a += 1
end
puts a
==== until-do-inline
	a = 1
	until a >= 10 do a += 1; end
puts a
==
a = 1
until a >= 10 do a += 1; end
puts a
==== until-modifier
	a = 1
	a += 1 until a >= 10
puts a
==
a = 1
a += 1 until a >= 10
puts a
==== until-empty
z = 1
begin

end until z == 1
==
z = 1
begin

end until z == 1
==== case
digit = 0
case age
when 0
puts "zero"
when 1
puts "one"
when 2
puts "two"
else
puts "other"
end
==
digit = 0
case age
	when 0
		puts "zero"
	when 1
		puts "one"
	when 2
		puts "two"
	else
		puts "other"
end
==== case-else-only
case
else
puts "other"
end
==
case
	else
		puts "other"
end
==== case-empty
case z
when 1

when 2

end
==
case z
	when 1

	when 2

end
====case-else-empty
case z

when 1

when 2

else

end
==
case z

	when 1

	when 2

	else

end
==== case-compact
		case opts[1] when 'utf-8'
	puts "YES"
end
==
case opts[1] when 'utf-8'
		puts "YES"
end
==== BEGIN
BEGIN {
doStuff
}
==
BEGIN {
	doStuff
}
==== BEGIN-comments
BEGIN {
#begin
doStuff
}
==
BEGIN {
	#begin
	doStuff
}
==== BEGIN-empty
BEGIN {
}
==
BEGIN {
}
==== END
END {
doStuff
}
==
END {
	doStuff
}
==== END-comments
END {
#end
doStuff
}
==
END {
	#end
	doStuff
}
==== lambda-declarations
def func()
foo()
a = lambda {
print "a"
}
a.call
foo()
print "zzzzzzzzzz"
#comment
end
==
def func()
	foo()
	a = lambda {
		print "a"
	}
	a.call
	foo()
	print "zzzzzzzzzz"
	#comment
end
