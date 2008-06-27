if $0 == __FILE__
  require 'test/unit/testsuite'
  $LOAD_PATH << '..'

  # disable logging
  require 'common/Logger'
  require 'common/NullLogManager'
  require 'common/StdoutLogManager'
  module XoredDebugger
    Logger.setup(StdoutLogManager.new)
  end
end

require 'test/unit'
require 'dbgp/Utils.rb'

module XoredDebugger    
  class UtilsTest < Test::Unit::TestCase
    include XoredDebuggerUtils

    def assert_convertion(uri,file)
      assert_equal uri, path_to_uri(file)
      assert_equal file, uri_to_path(uri)
    end

    def test_pathToURI_windows
      assert_convertion 'file:///c:/windows', 'c:/windows'
    end

    def test_pathToURI_unix
      assert_convertion 'file:///home/user', '/home/user'
    end

    def test_pathToURI_special_chars
      assert_convertion 'file:///home%5B%5D', '/home[]'
    end

  end
end
