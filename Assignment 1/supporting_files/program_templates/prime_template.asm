	.data
a:
	13
	.text
main:
    load %x0, $a, %x3       
    addi %x4, 2, %x4 
loop:
	beq %x4, %x3, true
	div %x3, %x4, %x5
	mul %x5, %x4, %x6
	sub %x3, %x6, %x6
	beq %x6, 0, false
	addi %x4, 1, %x4
	jmp loop
false:
	subi %x0, 1, %x10
	end
true:
	addi %x0, 1, %x10
	end