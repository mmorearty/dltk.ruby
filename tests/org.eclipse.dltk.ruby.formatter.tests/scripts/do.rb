==== simple-do
num = 1
7.times do
print num
num += 1
end
==
num = 1
7.times do
	print num
	num += 1
end
==== simple-do2
[ 1, 2, 3 ].each do |x|
print x
end
==
[ 1, 2, 3 ].each do |x|
	print x
end
==== simple-do-braces
num = 1
7.times {
print num
num += 1
}
==
num = 1
7.times {
	print num
	num += 1
}
==== simple-do-braces2
[ 1, 2, 3 ].each { |x|
print x
}
==
[ 1, 2, 3 ].each { |x|
	print x
}
==== simple-do-braces-empty
[ 1, 2, 3 ].each { |x|

}
==
[ 1, 2, 3 ].each { |x|

}
==== simple-do-empty
[ 1, 2, 3 ].each do |x|

end
==
[ 1, 2, 3 ].each do |x|

end
