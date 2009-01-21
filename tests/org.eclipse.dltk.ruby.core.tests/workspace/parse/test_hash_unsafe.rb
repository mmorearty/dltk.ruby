###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

#JRubySourceParser.HASH_FIXER_UNSAFE1
a :b =>
C.a :b =>

 a :b =>
 C.a :b =>

a :b => 
C.a :b => 

 a :b => 
 C.a :b => 

a(:b =>)
C.a(:b =>)

 a(:b =>)
 C.a(:b =>)

a(:b =>) 
C.a(:b =>) 

 a(:b =>) 
 C.a(:b =>) 

a( :b => )
C.a( :b => )

 a( :b => )
 C.a( :b => )

a( :b => ) 
C.a( :b => ) 

 a( :b => ) 
 C.a( :b => ) 


#JRubySourceParser.HASH_FIXER_UNSAFE1
a 'b' =>
C.a 'b' =>

 a 'b' =>
 C.a 'b' =>

a 'b' => 
C.a 'b' => 

 a 'b' => 
 C.a 'b' => 

a('b' =>)
C.a('b' =>)

 a('b' =>)
 C.a('b' =>)

a('b' =>) 
C.a('b' =>) 

 a('b' =>) 
 C.a('b' =>) 

a( 'b' => )
C.a( 'b' => )

 a( 'b' => )
 C.a( 'b' => )

a( 'b' => ) 
C.a( 'b' => ) 

 a( 'b' => ) 
 C.a( 'b' => ) 


#JRubySourceParser.HASH_FIXER_UNSAFE1
a "b" =>
C.a "b" =>

 a "b" =>
 C.a "b" =>

a "b" => 
C.a "b" => 

 a "b" => 
 C.a "b" => 

a("b" =>)
C.a("b" =>)

 a("b" =>)
 C.a("b" =>)

a("b" =>) 
C.a("b" =>) 

 a("b" =>) 
 C.a("b" =>) 

a( "b" => )
C.a( "b" => )

 a( "b" => )
 C.a( "b" => )

a( "b" => ) 
C.a( "b" => ) 

 a( "b" => ) 
 C.a( "b" => ) 


#JRubySourceParser.HASH_FIXER_UNSAFE2

:a => :b

:a => 'b'

:a => "b"

 :a => :b
 :a => 'b'
 :a => "b"
 
  :a => :b
  :a => 'b'
  :a => "b"

'a' => :b

'a' => 'b'

'a' => "b"

 'a' => :b
 'a' => 'b'
 'a' => "b"
 
  'a' => :b
  'a' => 'b'
  'a' => "b"

"a" => :b

"a" => 'b'

"a" => "b"

 "a" => :b
 "a" => 'b'
 "a" => "b"
 
  "a" => :b
  "a" => 'b'
  "a" => "b"


a :k1 => 'v1', :k
b(:kk1 => 'vv1',
  :kk2 => 3)

c :k1 => 'v1', :k
d(:kk1 => 'vv1',
  :kk2 => 'vv2',
  :kk3 => 3)

e 'k1' => 'v1', 'k'
f('kk1' => 'vv1',
  'kk2' => 3)

g 'k1' => 'v1', 'k'
h('kk1' => 'vv1',
  'kk2' => 'vv2',
  'kk3' => 3)

i "k1" => "v1", "k"
j("kk1" => "vv1",
  "kk2" => 3)

k "k1" => "v1", "k"
l("kk1" => "vv1",
  "kk2" => "vv2",
  "kk3" => 3)


A.new :k1 => 'v1', :k
B.new(:kk1 => 'vv1',
  :kk2 => 3)

C.new :k1 => 'v1', :k
D.new(:kk1 => 'vv1',
  :kk2 => 'vv2',
  :kk3 => 3)

E.new 'k1' => 'v1', 'k'
F.new('kk1' => 'vv1',
  'kk2' => 3)

G.new 'k1' => 'v1', 'k'
H.new('kk1' => 'vv1',
  'kk2' => 'vv2',
  'kk3' => 3)

I.new "k1" => "v1", "k"
J.new("kk1" => "vv1",
  "kk2" => 3)

K.new "k1" => "v1", "k"
L.new("kk1" => "vv1",
  "kk2" => "vv2",
  "kk3" => 3)
