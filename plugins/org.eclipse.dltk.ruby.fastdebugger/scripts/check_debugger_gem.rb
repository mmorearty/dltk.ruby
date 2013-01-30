###############################################################################
# Copyright (c) 2012 Tristan Hume
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

# This script checks if the required debugger gem is installed
require "rubygems"
begin
  require 'ruby-debug-base'
  puts 'true'
rescue LoadError
  puts 'false'
end