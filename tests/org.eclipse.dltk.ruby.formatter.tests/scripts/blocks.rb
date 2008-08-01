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
