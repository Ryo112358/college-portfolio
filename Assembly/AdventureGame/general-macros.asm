######## General Purpose Macros ########

# Print a string in memory --------------------
.macro print_string(%str_name)
	li $v0, 4
	la $a0, %str_name
	syscall
.end_macro

.macro print_strings(%str1, %str2)
	li $v0, 4
	la $a0, %str1
	syscall
	la $a0, %str2
	syscall
.end_macro

.macro print_strings(%str1, %str2, %str3)
	li $v0, 4
	la $a0, %str1
	syscall
	la $a0, %str2
	syscall
	la $a0, %str3
	syscall
.end_macro

.macro print_integer(%register)
	li $v0, 1
	move $a0, %register
	syscall
.end_macro

# Print a newline -----------------------------
.macro print_newline()
	li $v0, 4
	la $a0, newline
	syscall
.end_macro

# Print new string -----------------------------
.macro print(%str)
	.data
		string: .asciiz %str
	.text
		li $v0, 4
		la $a0, string
		syscall
.end_macro

# Print new string w/ newline -----------------
.macro println(%str)
	.data
		string: .asciiz %str
	.text
		li $v0, 4
		la $a0, string
		syscall
		la $a0, newline
		syscall
.end_macro

# Print specified num of newlines -------------
.macro print_newlines(%count)
	li $t9, %count
	li $t8, 0
	
	li $v0, 4
	la $a0, newline
LOOP:
	syscall
	addi $t8, $t8, 1
	bne $t8, $t9, LOOP
.end_macro

# Get integer from user ------------------------
# + Specify message & register to hold value ---
.macro get_int_from_user(%message, %register)
	print(%message)
	li $v0, 5
	syscall
	move %register, $v0
.end_macro

.macro get_digit_from_user(%message, %register)
	print(%message)
	li $v0, 12	# Gets ascii character code
	syscall
	addi $v0, $v0, -48	# Remove ascii offset
	move %register, $v0
.end_macro

# Exit program ---------------------------------
.macro EXIT
	li $v0, 10
	syscall
.end_macro


# Common/Useful Data Values --------------------
.data
	newline: .asciiz "\n"
