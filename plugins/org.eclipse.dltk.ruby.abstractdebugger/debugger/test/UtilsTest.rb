require 'test/unit'
require 'dbgp/Utils.rb'

module XoredDebugger    
  class UtilsTest < Test::Unit::TestCase
    include XoredDebuggerUtils

    def test_pathToURI_windows
      assert_equal 'file:///c:/windows', path_to_uri('c:/windows')
    end

    def test_pathToURI_unix
      assert_equal 'file:///home/user', path_to_uri('/home/user')
    end

    def test_pathToURI_special_chars
      assert_equal 'file:///home%5B%5D', path_to_uri('/home[]')
    end

  end
end
