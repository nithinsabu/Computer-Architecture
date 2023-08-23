	.data
n:
	3
	.text
main:
	load %x0, $n, %x3
	addi %x0, 1, %x20
	addi %x0, 65535, %x4
	beq %x3, %x20, exception
	addi %x0, 2, %x11
	addi %x0, 0, %x5
	addi %x0, 1, %x6
	store %x5, 0, %x4
	subi %x4, 1, %x4
	store %x6, 0, %x4
loop:
	beq %x3, %x11, exit
	subi %x3, 1, %x3
	subi %x4, 1, %x4
	add %x5, %x6, %x6
	sub %x6, %x5, %x5
	store %x6, 0, %x4
	jmp loop
exception:
	store %x0, 0, %x4
exit:
	end