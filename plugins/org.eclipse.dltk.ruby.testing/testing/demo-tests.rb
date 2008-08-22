require 'shoulda'
require 'test/unit'

class User
	attr_accessor :full_name
	def initialize(name)
		@full_name = name
	end

end

class MathTest < Test::Unit::TestCase
	def test1
		
	end
	
	def test2
		
	end
end

class UserTest < Test::Unit::TestCase
	context "A User instance" do
		setup do
			@user = User.new('John Doe')
		end

		should "return its full name" do
			assert_equal 'John Doe', @user.full_name
		end

		should "failing example" do
			assert_equal 'JohN Doe', @user.full_name
		end
		
		should "raise Exception" do
			raise StandardError		end

		should "not nil" do
			assert_not_nil @user 
		end

	end
end
