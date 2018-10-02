######### Generate a Random Integer ##########

# Print a newline -----------------------------
.macro print_newline()
	li $v0, 4
	la $a0, newline_grm
	syscall
.end_macro

# Store current time to be used as seed -------
.macro get_time()
	li $v0, 30
	syscall
	
	move $t0, $a0
.end_macro

# Generate random seed ------------------------
.macro generate_seed()
	get_time()
	li	$a0,1
	move	$a1, $t0
	li	$v0, 40
	syscall
.end_macro

# Get random integer in range -----------------
.macro get_random_int(%upperbound)
	li $v0, 42
	li $a0, 1
	li $a1, %upperbound
	addi $a1, $a1, 1	# Make upperbound inclusive
	syscall
.end_macro

.macro get_random_int(%lowerbound, %upperbound)
	li $v0, 42
	li $a0, 1
	li $a1, %upperbound
	sub $a1, $a1, %lowerbound
	addi $a1, $a1, 1
	syscall
	addi $a0, $a0, %lowerbound
.end_macro

.macro get_random_int_registers(%lowerbound, %upperbound)
	li $v0, 42
	li $a0, 1
	move $a1, %upperbound
	sub $a1, $a1, %lowerbound
	addi $a1, $a1, 1
	syscall
	add $a0, $a0, %lowerbound
.end_macro

# Display random integers ---------------------
.macro announce_random(%upperbound)
	li $v0, 4
	la $a0, random_1a
	syscall
	li $v0, 1
	li $a0, %upperbound
	syscall
	li $v0, 4
	la $a0, random_1b
	syscall
.end_macro

.macro print_random_result(%upperbound)
	announce_random(%upperbound)
	get_random_int(%upperbound)
	li $v0, 1
	syscall
	print_newline()
.end_macro

.macro announce_random(%lowerbound, %upperbound)
	li $v0, 4
	la $a0, random_2a
	syscall
	li $v0, 1
	li $a0, %lowerbound
	syscall
	li $v0, 4
	la $a0, random_2b
	syscall
	li $v0, 1
	li $a0, %upperbound
	syscall
	li $v0, 4
	la $a0, random_2c
	syscall
.end_macro

.macro print_random_result(%lowerbound, %upperbound)
	announce_random(%lowerbound, %upperbound)
	get_random_int(%lowerbound, %upperbound)
	li $v0, 1
	syscall
	print_newline()
.end_macro

############## Test Program ###################
.data
	random_1a: .asciiz "Random number (0 <= x <= "
	random_1b: .asciiz "): "
	
	random_2a: .asciiz "Random number ("
	random_2b: .asciiz " <= x <= "
	random_2c: .asciiz "): "
	
	newline_grm:	.asciiz "\n"

.text

MAIN_GRM:
	generate_seed()
	
	j DONE_GRM
		
	li $t8, 0
	li $t9, 25
	
LOOP:
	print_random_result(13)
	print_random_result(5,18)
	addi $t8, $t8, 1
	bne $t8, $t9, LOOP

DONE_GRM:
