#!/bin/sh

###### Change these to tastes ######
JAY=jay
RUBY=ruby
PARSER_BASE=DefaultRubyParser
YYTABLE_PREFIX=
###### Do not change below ######

echo "Generating Parser '$PARSER_BASE' w/ YYTable prefix of '$YYTABLE_PREFIX'"

# Generate grammar as intermediate file
$JAY $PARSER_BASE.y < skeleton.parser | grep -v "^//t" >$PARSER_BASE.out

# Patch file to get around Java static initialization issues plus extract
# a bunch of stuff to seperate file (yytables).
$RUBY ../../../../bin/patch_parser.rb $PARSER_BASE.out $YYTABLE_PREFIX > $PARSER_BASE.java
rm -f $PARSER_BASE.out
