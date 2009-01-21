###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

#JRubySourceParser.COMMA_FIXER1
a :b =>,do
end
C.a :b =>,do
end

a :b =>, do
end
C.a :b =>, do
end

a(:b =>,)do
end
C.a(:b =>,)do
end

a( :b =>, ) do
end
C.a( :b =>, ) do
end


#JRubySourceParser.COMMA_FIXER2
d e,do
end
C.d e,do
end

d e, do
end
C.d e, do
end

d(e,)do
end
C.d(e,)do
end

d( e, ) do
end
C.d( e, ) do
end


#JRubySourceParser.COMMA_FIXER3A
f 'g' => 'h', 'i' do
end
C.f 'g' => 'h', 'i' do
end

f('g' => 'h', 'i') do
end
C.f('g' => 'h', 'i') do
end

f( 'g' => 'h', 'i' ) do
end
C.f( 'g' => 'h', 'i' ) do
end


#JRubySourceParser.COMMA_FIXER3B
f "g" => "h", "i" do
end
C.f "g" => "h", "i" do
end

f("g" => "h", "i") do
end
C.f("g" => "h", "i") do
end

f( "g" => "h", "i" ) do
end
C.f( "g" => "h", "i" ) do
end


#JRubySourceParser.COMMA_FIXER3C
f :g => :h, :i do
end
C.f :g => :h, :i do
end

f(:g => :h, :i) do
end
C.f(:g => :h, :i) do
end

f( :g => :h, :i ) do
end
C.f( :g => :h, :i ) do
end


#JRubySourceParser.COMMA_FIXER4
f(:g => :h,)
C.f(:g => :h,)

f( :g => :h, )
C.f( :g => :h, )


#JRubySourceParser.COMMA_FIXER5
f(g,)
C.f(g,)

f( g, )
C.f( g, )


#JRubySourceParser.COMMA_FIXER6
f(:g => :h, :i)
C.f(:g => :h, :i)

f( :g => :h, :i )
C.f( :g => :h, :i )

#JRubySourceParser.COMMA_FIXER6A
f('g' => 'h', 'i')
C.f('g' => 'h', 'i')

f( 'g' => 'h', 'i' )
C.f( 'g' => 'h', 'i' )

#JRubySourceParser.COMMA_FIXER6B
f("g" => "h", "i")
C.f("g" => "h", "i")

f( "g" => "h", "i" )
C.f( "g" => "h", "i" )
