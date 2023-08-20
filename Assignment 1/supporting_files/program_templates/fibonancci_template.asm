	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 6553, %x4
	addi %x0, 1, %x5
	muli %x4, 65533, %x6
	end