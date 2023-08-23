	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3
	subi %x3, 1, %x4
	add %x0, %x0, %x5
loop:
	beq %x5, %x4, exit
	addi %x5, 0, %x7
	addi %x5, 1, %x6
	jmp innerloop
innerloop:
	beq %x6, %x3, increment
	load %x6, $a, %x20
	load %x7, $a, %x21
	bgt %x20, %x21, update
	addi %x6, 1, %x6
	jmp innerloop
update:
	addi %x6, 0, %x7
	addi %x6, 1, %x6
	jmp innerloop
increment:
	bne %x5, %x7, swap
	addi %x5, 1, %x5
	jmp loop
swap:
	load %x5, $a, %x25
	load %x7, $a, %x26
	store %x26, $a, %x5
	store %x25, $a, %x7
	addi %x5, 1, %x5
	jmp loop
exit:
	store %x0, $a, %x3
	end