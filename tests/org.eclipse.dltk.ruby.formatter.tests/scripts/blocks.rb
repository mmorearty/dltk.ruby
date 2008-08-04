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
