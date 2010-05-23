###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

class DLTKBuiltinGeneratorSet
  include Enumerable

  # Creates a new set containing the given objects.
  def self.[](*ary)
    new(ary)
  end

  # Creates a new set containing the elements of the given enumerable
  # object.
  #
  # If a block is given, the elements of enum are preprocessed by the
  # given block.
  def initialize(enum = nil, &block) # :yields: o
    @hash ||= Hash.new

    enum.nil? and return

    if block
      enum.each { |o| add(block[o]) }
    else
      merge(enum)
    end
  end

  # Copy internal hash.
  def initialize_copy(orig)
    @hash = orig.instance_eval{@hash}.dup
  end

  # Returns the number of elements.
  def size
    @hash.size
  end
  alias length size

  # Returns true if the set contains no elements.
  def empty?
    @hash.empty?
  end

  # Removes all elements and returns self.
  def clear
    @hash.clear
    self
  end

  # Replaces the contents of the set with the contents of the given
  # enumerable object and returns self.
  def replace(enum)
    if enum.class == self.class
      @hash.replace(enum.instance_eval { @hash })
    else
      enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
      clear
      enum.each { |o| add(o) }
    end

    self
  end

  # Converts the set to an array.  The order of elements is uncertain.
  def to_a
    @hash.keys
  end

  def flatten_merge(set, seen = DLTKBuiltinGeneratorSet.new)
    set.each { |e|
      if e.is_a?(DLTKBuiltinGeneratorSet)
	if seen.include?(e_id = e.object_id)
	  raise ArgumentError, "tried to flatten recursive Set"
	end

	seen.add(e_id)
	flatten_merge(e, seen)
	seen.delete(e_id)
      else
	add(e)
      end
    }

    self
  end
  protected :flatten_merge

  # Returns a new set that is a copy of the set, flattening each
  # containing set recursively.
  def flatten
    self.class.new.flatten_merge(self)
  end

  # Equivalent to Set#flatten, but replaces the receiver with the
  # result in place.  Returns nil if no modifications were made.
  def flatten!
    if detect { |e| e.is_a?(DLTKBuiltinGeneratorSet) }
      replace(flatten())
    else
      nil
    end
  end

  # Returns true if the set contains the given object.
  def include?(o)
    @hash.include?(o)
  end
  alias member? include?

  # Returns true if the set is a superset of the given set.
  def superset?(set)
    set.is_a?(DLTKBuiltinGeneratorSet) or raise ArgumentError, "value must be a set"
    return false if size < set.size
    set.all? { |o| include?(o) }
  end

  # Returns true if the set is a proper superset of the given set.
  def proper_superset?(set)
    set.is_a?(DLTKBuiltinGeneratorSet) or raise ArgumentError, "value must be a set"
    return false if size <= set.size
    set.all? { |o| include?(o) }
  end

  # Returns true if the set is a subset of the given set.
  def subset?(set)
    set.is_a?(DLTKBuiltinGeneratorSet) or raise ArgumentError, "value must be a set"
    return false if set.size < size
    all? { |o| set.include?(o) }
  end

  # Returns true if the set is a proper subset of the given set.
  def proper_subset?(set)
    set.is_a?(DLTKBuiltinGeneratorSet) or raise ArgumentError, "value must be a set"
    return false if set.size <= size
    all? { |o| set.include?(o) }
  end

  # Calls the given block once for each element in the set, passing
  # the element as parameter.
  def each
    @hash.each_key { |o| yield(o) }
    self
  end

  # Adds the given object to the set and returns self.  Use +merge+ to
  # add several elements at once.
  def add(o)
    @hash[o] = true
    self
  end
  alias << add

  # Adds the given object to the set and returns self.  If the
  # object is already in the set, returns nil.
  def add?(o)
    if include?(o)
      nil
    else
      add(o)
    end
  end

  # Deletes the given object from the set and returns self.  Use +subtract+ to
  # delete several items at once.
  def delete(o)
    @hash.delete(o)
    self
  end

  # Deletes the given object from the set and returns self.  If the
  # object is not in the set, returns nil.
  def delete?(o)
    if include?(o)
      delete(o)
    else
      nil
    end
  end

  # Deletes every element of the set for which block evaluates to
  # true, and returns self.
  def delete_if
    @hash.delete_if { |o,| yield(o) }
    self
  end

  # Do collect() destructively.
  def collect!
    set = self.class.new
    each { |o| set << yield(o) }
    replace(set)
  end
  alias map! collect!

  # Equivalent to Set#delete_if, but returns nil if no changes were
  # made.
  def reject!
    n = size
    delete_if { |o| yield(o) }
    size == n ? nil : self
  end

  # Merges the elements of the given enumerable object to the set and
  # returns self.
  def merge(enum)
    if enum.is_a?(DLTKBuiltinGeneratorSet)
      @hash.update(enum.instance_eval { @hash })
    else
      enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
      enum.each { |o| add(o) }
    end

    self
  end

  # Deletes every element that appears in the given enumerable object
  # and returns self.
  def subtract(enum)
    enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
    enum.each { |o| delete(o) }
    self
  end

  # Returns a new set built by merging the set and the elements of the
  # given enumerable object.
  def |(enum)
    enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
    dup.merge(enum)
  end
  alias + |		##
  alias union |		##

  # Returns a new set built by duplicating the set, removing every
  # element that appears in the given enumerable object.
  def -(enum)
    enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
    dup.subtract(enum)
  end
  alias difference -	##

  # Returns a new set containing elements common to the set and the
  # given enumerable object.
  def &(enum)
    enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
    n = self.class.new
    enum.each { |o| n.add(o) if include?(o) }
    n
  end
  alias intersection &	##

  # Returns a new set containing elements exclusive between the set
  # and the given enumerable object.  (set ^ enum) is equivalent to
  # ((set | enum) - (set & enum)).
  def ^(enum)
    enum.is_a?(Enumerable) or raise ArgumentError, "value must be enumerable"
    n = DLTKBuiltinGeneratorSet.new(enum)
    each { |o| if n.include?(o) then n.delete(o) else n.add(o) end }
    n
  end

  # Returns true if two sets are equal.  The equality of each couple
  # of elements is defined according to Object#eql?.
  def ==(set)
    equal?(set) and return true

    set.is_a?(DLTKBuiltinGeneratorSet) && size == set.size or return false

    hash = @hash.dup
    set.all? { |o| hash.include?(o) }
  end

  def hash	# :nodoc:
    @hash.hash
  end

  def eql?(o)	# :nodoc:
    return false unless o.is_a?(DLTKBuiltinGeneratorSet)
    @hash.eql?(o.instance_eval{@hash})
  end

  # Classifies the set by the return value of the given block and
  # returns a hash of {value => set of elements} pairs.  The block is
  # called once for each element of the set, passing the element as
  # parameter.
  #
  # e.g.:
  #
  #   require 'set'
  #   files = Set.new(Dir.glob("*.rb"))
  #   hash = files.classify { |f| File.mtime(f).year }
  #   p hash    # => {2000=>#<Set: {"a.rb", "b.rb"}>,
  #             #     2001=>#<Set: {"c.rb", "d.rb", "e.rb"}>,
  #             #     2002=>#<Set: {"f.rb"}>}
  def classify # :yields: o
    h = {}

    each { |i|
      x = yield(i)
      (h[x] ||= self.class.new).add(i)
    }

    h
  end

  # Divides the set into a set of subsets according to the commonality
  # defined by the given block.
  #
  # If the arity of the block is 2, elements o1 and o2 are in common
  # if block.call(o1, o2) is true.  Otherwise, elements o1 and o2 are
  # in common if block.call(o1) == block.call(o2).
  #
  # e.g.:
  #
  #   require 'set'
  #   numbers = Set[1, 3, 4, 6, 9, 10, 11]
  #   set = numbers.divide { |i,j| (i - j).abs == 1 }
  #   p set     # => #<Set: {#<Set: {1}>,
  #             #            #<Set: {11, 9, 10}>,
  #             #            #<Set: {3, 4}>,
  #             #            #<Set: {6}>}>
  def divide(&func)
    if func.arity == 2
      require 'tsort'

      class << dig = {}		# :nodoc:
	include TSort

	alias tsort_each_node each_key
	def tsort_each_child(node, &block)
	  fetch(node).each(&block)
	end
      end

      each { |u|
	dig[u] = a = []
	each{ |v| func.call(u, v) and a << v }
      }

      set = DLTKBuiltinGeneratorSet.new()
      dig.each_strongly_connected_component { |css|
	set.add(self.class.new(css))
      }
      set
    else
	DLTKBuiltinGeneratorSet.new(classify(&func).values)
    end
  end

  InspectKey = :__inspect_key__         # :nodoc:

  # Returns a string containing a human-readable representation of the
  # set. ("#<Set: {element1, element2, ...}>")
  def inspect
    ids = (Thread.current[InspectKey] ||= [])

    if ids.include?(object_id)
      return sprintf('#<%s: {...}>', self.class.name)
    end

    begin
      ids << object_id
      return sprintf('#<%s: {%s}>', self.class, to_a.inspect[1..-2])
    ensure
      ids.pop
    end
  end

  def pretty_print(pp)	# :nodoc:
    pp.text sprintf('#<%s: {', self.class.name)
    pp.nest(1) {
      pp.seplist(self) { |o|
	pp.pp o
      }
    }
    pp.text "}>"
  end

  def pretty_print_cycle(pp)	# :nodoc:
    pp.text sprintf('#<%s: {%s}>', self.class.name, empty? ? '' : '...')
  end
end

class DLTKBuiltinGenerator

def collectMethod(hash, methodName, arity)
	hash.add methodName + '###' + arity.to_s
end

def put_methods!(metaclass)
	result = DLTKBuiltinGeneratorSet.new
	modules = metaclass.included_modules

	methods = metaclass.public_instance_methods(false)
	for mod in modules do; methods -= mod.public_instance_methods(true); end

	$data << "\t\npublic\n"
	for method in methods do
		arity = metaclass.instance_method(method).arity
		collectMethod(result, method.to_s, arity)
		put_rdoc(metaclass.name + "." + method.to_s)
		$data << <<-"END"
	def #{method.to_s}(#{generateArgs(arity)})
	end

END
	end

	methods = metaclass.protected_instance_methods(false)
	for mod in modules do;
		if mod.respond_to?(:protected_instance_methods) then
			methods -= mod.protected_instance_methods(true)
			end
	end

	$data << "\t\nprotected\n"
	for method in methods do
		put_rdoc(metaclass.name + "." + method.to_s)
		arity = metaclass.instance_method(method).arity
		collectMethod(result, method.to_s, arity)
		$data << <<-"END"
	def #{method.to_s}(#{generateArgs(arity)})
	end

END
	end


	methods = metaclass.private_instance_methods(false)
	for mod in modules do;
		if mod.respond_to?(:private_instance_methods) then
			methods -= mod.private_instance_methods(true)
		end
	end

	$data << "\t\nprivate\n"
	for method in methods do
		put_rdoc(metaclass.name + "." + method.to_s)
		arity = metaclass.instance_method(method).arity
		collectMethod(result, method.to_s, arity)
		$data << <<-"END"
	def #{method.to_s}(#{generateArgs(arity)})
	end

END
	end

	return result

end

def generateArgs(arity)
	if arity < 0 then
		return "*args"
	end
	result = ""
	for i in 1..arity do
		result <<  "arg" + i.to_s
		if i != arity then
			result << ", "
		end
	end
	result
end

def put_singleton_methods!(metaclass, instanceMethods)
#	if (metaclass.is_a?(Class))
#		$data << <<-"END2"
#		class << self
#		END2
#	else
#		$data << "\t\tprivate\n"
#	end
#	ms = (metaclass.public_methods(false) - Class.instance_methods(false)).each { |m|
	ms = (metaclass.singleton_methods(false)).each { |m|
		arity = metaclass.method(m).arity
		if (!instanceMethods.include?(m.to_s + '###' + arity.to_s)) then
			$data << <<-"END2"
	def self.#{m.to_s}(#{generateArgs(arity)})
	end

			END2
		end
	}
#	if (metaclass.is_a?(Class))
#		$data << <<-"END2"
#		end
#		END2
#	end
end

def put_included_modules!(metaclass)

	ms = (metaclass.included_modules -
			((metaclass.is_a?(Class) &&
			metaclass.superclass &&
				metaclass.superclass.included_modules) || []))

	for in_mod in ms do
		$data << <<-"END"
	include #{in_mod.name}
		END
	end

end

def put_singleton_included_modules!(metaclass)
	$data << <<-"END2"
	class << ::#{metaclass.name}
	END2

	sup = (class << metaclass; superclass; end)
#	ms = ((class << metaclass; included_modules; end) -
#			((sup && sup.included_modules) || []))
	ms = (class << metaclass; included_modules; end)


	for in_mod in ms do
		$data << <<-"END"
		include #{in_mod.name}
		END
	end

	$data << <<-"END2"
	end
	END2

end

def put_constants(metaclass)
	#return
	consts = DLTKBuiltinGeneratorSet.new(metaclass.constants)
	ancestors = metaclass.ancestors
	for m in ancestors
		if m != metaclass
			consts -= DLTKBuiltinGeneratorSet.new(m.constants)
		end
	end 
	for c in consts
		v = metaclass.const_get(c)
		if !@classesAndModules.include?(v)
			$data << c.to_s + '=' + v.class.to_s + ".new\n"  
		end
	end
end

def put_superclass!(metaclass)
	return if metaclass.superclass.nil?
	$data << <<-"END"
		#{const_for(metaclass)}.setSuperClass(#{const_for(metaclass.superclass)});
	END
end

#def put_singleton_superclass!(metaclass)
#	$data << <<-"END"
#		#{const_for_singleton(metaclass)}.setSuperClass(#{const_for(metaclass.class)});
#	END
#end



def dumpClass(klass)
	name = klass.name
	if name == "NameError::message" || name == "fatal" then
		return
	end
	sc = klass.superclass
	if sc then scname = " < ::" + sc.name else scname = "" end
#	methods = klass.instance_methods(false)
	put_rdoc(name)
	$data << <<-"END"

class #{name} #{scname}
	END
	put_included_modules!(klass)
	if sc then
		# skip Object - they are generated later
		put_constants(klass)
	end
	put_singleton_included_modules!(klass)
	instanceMethods = put_methods!(klass)
	put_singleton_methods!(klass, instanceMethods)
	$data << <<-"END"
end

	END
end

def put_rdoc(name)
	return
	print "Getting doc for " + name + "\n"
	rdoc = `d:\\instantrails\\ruby\\bin\\ri.bat \"#{name}\" -f html`
	rdoc.to_a.each { |line|
		$data << "#" + line
	}
end

def dumpModule(klass)
	name = klass.name
	#	methods = klass.instance_methods(false)
	put_rdoc(name)
	$data << <<-"END"

module #{name}
	END
	put_included_modules!(klass)
	put_constants(klass)
	instanceMethods = put_methods!(klass)
	put_singleton_methods!(klass, instanceMethods)
	$data << <<-"END"
end

	END
end

def dumpVariables
	$data << "# Global variables\n"
	global_variables.each { |var|
		$data << <<-"END"
#{var.to_s} = #{var.to_s}
		END
	}
end


def process_all
	classes = DLTKBuiltinGeneratorSet.new
	known_modules = DLTKBuiltinGeneratorSet.new
	ObjectSpace.each_object do |o|
		if (o.respond_to?('name')) then
			className = o.name
			if className == 'DLTKBuiltinGenerator' || className == 'DLTKBuiltinGeneratorSet' then 
				next
			end
		end
		classes << o if o.is_a?(Class)
		known_modules << o if o.is_a?(Module)
	end
	@classesAndModules = known_modules + classes
	known_modules = (known_modules - classes).to_a
	classes = classes.to_a.sort { |a,b| a.name <=> b.name }

	classes.each { |c|
		$data = ""
		dumpClass(c)
		filename = c.name
		pos = filename.index(':')
		if pos then
			filename = filename.slice(0..pos-1)
			#puts "sliced " + filename
		end
		file = filename + ".rb"
		puts "#### DLTK RUBY BUILTINS ####" + file + "\n"
		puts $data
		#File.open(file, 'a') {|f| f.write $data}
	}
	known_modules.each { |c|
		$data = ""
		dumpModule(c)
		filename = c.name
		pos = filename.index(':')
		if pos then
			filename = filename.slice(0..pos-1)
			#puts "sliced " + filename
		end
		file = filename + ".rb"
		puts "#### DLTK RUBY BUILTINS ####" + file + "\n"
		puts $data
		#File.open(file, 'a') {|f| f.write $data}
	}

	ccc = []
	Module.constants.each { |x|
		ccc << x.to_s 
	}
	ccc.delete('DLTKBuiltinGenerator')
	ccc.delete('DLTKBuiltinGeneratorSet')
	known_modules.each { |x|
		ccc.delete(x.to_s)
	}
	classes.each { |x|
		ccc.delete(x.to_s)
	}

	puts "#### DLTK RUBY BUILTINS ####constants.rb\n\n\n"
	ccc.each { |bar|
		puts "#{bar} = #{Module.const_get(bar).class.to_s}.new"
	}

end

end

g = DLTKBuiltinGenerator.new
g.process_all

# vim: noexpandtab tabstop=4 sts=0 sw=4
