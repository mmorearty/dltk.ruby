==== unary-plus-if-block
if a == 1
+a
end
==
if a == 1
	+a
end
==== unary-plus-method
def z
+a
end
==
def z
	+a
end
==== unary-plus-for-block-single
for i in [1...10]
+a    
end
==
for i in [1...10]
	+a    
end
==== unary-plus-for-block-multiple
for i in [1...10]
+a
print i    
end
==
for i in [1...10]
	+a
	print i    
end
==== unary-plus-do-block-single
[ 1, 2, 3 ].each do |x|
+a
end
==
[ 1, 2, 3 ].each do |x|
	+a
end
==== unary-plus-do-block-multiple
[ 1, 2, 3 ].each do |x|
+a
print x
end
==
[ 1, 2, 3 ].each do |x|
	+a
	print x
end
