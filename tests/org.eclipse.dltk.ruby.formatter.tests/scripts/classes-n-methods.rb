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
