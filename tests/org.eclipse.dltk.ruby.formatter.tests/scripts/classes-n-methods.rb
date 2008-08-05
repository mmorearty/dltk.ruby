==== empty class
class Hello

end
==
class Hello

end
==== attribute only
class Hello
attr_accessor :var
end
==
class Hello
	attr_accessor :var
end
==== 2 methods
class Hello
def method1
return 1 
end
def method2
return 2
end
end
==
class Hello
	def method1
		return 1 
	end
	def method2
		return 2
	end
end
==== method-incorrectly-indented
	def func()
foo()
#comment
end
==
def func()
	foo()
	#comment
end
==== 2 methods-empty lines
class Hello

def method1
return 1
end

def method2
return 2
end

end
==
class Hello

	def method1
		return 1
	end

	def method2
		return 2
	end

end
==== empty method
class Hello

def method1

end

def method2

end

end
==
class Hello

	def method1

	end

	def method2

	end

end
==== SClass
class << self
def execute
puts "execute"		
end
end
==
class << self
	def execute
		puts "execute"		
	end
end
==== empty-SClass
class << self
def execute
puts "execute"		
end
end
==
class << self
	def execute
		puts "execute"		
	end
end
