@echo off

rem ###### Change these to tastes ######
set JAY=jay
set RUBY=ruby
set PARSER_BASE=DefaultRubyParser
set YYTABLE_PREFIX=
rem ###### Do not change below ######

echo "Generating Parser '%PARSER_BASE%' w/ YYTable prefix of '%YYTABLE_PREFIX%'"

rem # Generate grammar as intermediate file
%JAY% %PARSER_BASE%.y < skeleton.parser | grep -v "^//t" >%PARSER_BASE%.out

rem # Patch file to get around Java static initialization issues plus extract
rem # a bunch of stuff to seperate file (yytables).
%RUBY% patch_parser.rb %PARSER_BASE%.out %YYTABLE_PREFIX% > %PARSER_BASE%.java
del %PARSER_BASE%.out
