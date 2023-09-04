import os
if not os.path.exists("jars/assembler.jar"):
	print ("compilation failed. jar file not created")
print("Current Working Directory:", os.getcwd())
