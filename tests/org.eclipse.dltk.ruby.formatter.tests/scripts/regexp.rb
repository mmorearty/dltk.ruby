==== regexp
s =~ /[a-z\
A-Z]/
==
s =~ /[a-z\
A-Z]/
==== regexp-method
def chab(s)   # "contains hex in angle brackets"
(s =~ /<0(x|
X)(\d|[a-f]|[A-F])+>/) != nil
end
==
def chab(s)   # "contains hex in angle brackets"
	(s =~ /<0(x|
X)(\d|[a-f]|[A-F])+>/) != nil
end
==== dregexp-method
def chab(s)   # "contains hex in angle brackets"
lowerCaseDigit = "[a-f]"
upperCaseDigit = "[A-F]"
(s =~ /<0(x|
X)(\d|#{lowerCaseDigit}|#{upperCaseDigit})+>/) != nil
end
==
def chab(s)   # "contains hex in angle brackets"
	lowerCaseDigit = "[a-f]"
	upperCaseDigit = "[A-F]"
	(s =~ /<0(x|
X)(\d|#{lowerCaseDigit}|#{upperCaseDigit})+>/) != nil
end
==== regexp-method-escaped
def chab(s)   # "contains hex in angle brackets"
(s =~ /<0(x|\
X)(\d|[a-f]|[A-F])+>/) != nil
end
==
def chab(s)   # "contains hex in angle brackets"
	(s =~ /<0(x|\
X)(\d|[a-f]|[A-F])+>/) != nil
end
==== regexp-until-modifier
i = "z"
begin
print i
i += "z"
end until (s =~/[a-z
A-Z]/) == nil
==
i = "z"
begin
	print i
	i += "z"
end until (s =~/[a-z
A-Z]/) == nil
