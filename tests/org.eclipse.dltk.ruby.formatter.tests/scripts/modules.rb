==== empty module
module SomeThing
VERSION = 1
end
==
module SomeThing
	VERSION = 1
end
==== nested module
module Some
module Thing
VERSION = 1
end
end
==
module Some
	module Thing
		VERSION = 1
	end
end
==== module with class
module Some
module Thing
VERSION = 1

class Hello

def execute
puts "Hello, world"
end

end

end
end
==
module Some
	module Thing
		VERSION = 1

		class Hello

			def execute
				puts "Hello, world"
			end

		end

	end
end
