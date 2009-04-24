==== rdoc-simple
=begin
Some
	Documentation
=end
class ABC
def method1
end
def method2
end
end
==
=begin
Some
	Documentation
=end
class ABC
	def method1
	end
	def method2
	end
end
====
==== nested rdocs
=begin
outside
	class
=end
class C
=begin
inside
	class
=end
def a
=begin
inside
	method
=end
end
def b
end
end
==
=begin
outside
	class
=end
class C
=begin
inside
	class
=end
	def a
=begin
inside
	method
=end
	end
	def b
	end
end
