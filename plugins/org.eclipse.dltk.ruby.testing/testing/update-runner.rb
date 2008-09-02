def loadFile(inputFileName)
	input = nil
	open inputFileName do |f|
		input = f.read
	end
	raise $! if input.nil?
	lines = input.split /\n/
	return lines
end

def extractRegion(lines, beginPattern, endPattern)
	output = nil
	active = false
	for l in lines do
		if l =~ beginPattern
			active = true
		elsif l =~ endPattern
			active = false
		else
			if active
				if output.nil?
					output = []					
				end
				output << l
			end
		end
	end
	return output
end

def replaceRegion(lines, newContent, beginPattern, endPattern)
	output = []					
	active = false
	for l in lines do
		if !active && l =~ beginPattern
			output << l
			active = true
			newContent.each { |line| output << line }
		elsif l =~ endPattern
			active = false
			output << l
		else
			if !active
				output << l
			end
		end
	end
	return output
end

MESSAGE_IDS_BEGIN = /module MessageIds/
MESSAGE_IDS_END   = /end # of MessageIds/

TARGET_FILE       = "dltk-rspec-runner.rb"

def main
	lines = loadFile("dltk-testunit-runner.rb")
	messageIds = extractRegion(lines, MESSAGE_IDS_BEGIN, MESSAGE_IDS_END)
	target = loadFile(TARGET_FILE)
	originalTarget = target.clone()
	if messageIds
		target = replaceRegion(target, messageIds, MESSAGE_IDS_BEGIN, MESSAGE_IDS_END)
	end
	if target != originalTarget
		puts "Updated"
		f = File.open(TARGET_FILE, "w+")
		f.puts target.join("\n")
		f.close
	end
end

main
