###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

#JRubySourceParser.COLON_FIXER1
A::

B:: 

c D::, e
C.c D::, e

e(F::)
C.e(F::)

g[H::]
C.g[H::]

h{I::}
C.h{I::}


#JRubySourceParser.COLON_FIXER2
j :k => :, :l => 2
C.j :k => :, :l => 2

j :k => : , :l => 2
C.j :k => : , :l => 2

j{k :l => :}
C.j{k :l => :}

j{k :l => : }
C.j{k :l => : }

j(k :l => :)
C.j(k :l => :)

j(k :l => : )
C.j(k :l => : )


#JRubySourceParser.COLON_FIXER3
j :, k
C.j :, k

j : , k
C.j : , k

j{k :}
C.j{k :}

j{k : }
C.j{k : }

j(k :)
C.j(k :)

j(k : )
C.j(k : )
