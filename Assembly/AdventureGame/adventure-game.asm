# Pulkit (@Ryo112358)

.include "general-macros.asm"
.include "generate-random-number.asm"

# Print macros ----------------------------------------------
.macro print_hero_w_space(%bool)
		li $v0, 4
		move $a0, $t6	# Hero address stored in $t6
		syscall

		li $t1, %bool
		bgtz $t1, PRINT_SPACE
		j DONE_HEROPRINT

	PRINT_SPACE:
		print_string(sym_space)

	DONE_HEROPRINT:
		move $t1, $0
.end_macro

.macro print_versus_msg(%enemy)
	li $v0, 4
	la $a0, text_pad_star
	syscall
	print_hero_w_space(1)
	la $a0, text_versus
	syscall
	la $a0, %enemy
	syscall
	print_integer($t4)
	li $v0, 4
	la $a0, text_pad_star
	syscall
	print_newline()
.end_macro

.macro print_versus_wizard()
	li $v0, 4
	la $a0, text_pad_star
	syscall
	print_hero_w_space(1)
	la $a0, text_versus
	syscall
	la $a0, evil_wizard
	syscall
	li $v0, 4
	la $a0, text_pad_star
	syscall
	print_newline()
.end_macro

.macro print_hero_attacks(%enemy)
	print_hero_w_space(1)
	print_strings(attack_msg, sym_equals)
	print_integer($s1)
	print_string(sym_plus)
	print_integer($t9)
	print_string(sym_equals)
	print_integer($t7)
	
	print_newline()
	
	print_strings(%enemy, hp_is_now)
	print_integer($s4)
	print_string(sym_minus)
	print_integer($t7)
	print_string(sym_equals)
	sub $s4, $s4, $t7	# Lower enemy health
	print_integer($s4)
	
	print_newlines(2)
.end_macro

.macro print_enemy_attacks(%enemy)
	print_string(%enemy)
	print_strings(attack_msg, sym_equals)
	print_integer($s5)
	print_string(sym_plus)
	print_integer($t9)
	print_string(sym_equals)
	print_integer($t7)
	
	print_newline()
	
	print_hero_w_space(1)
	print_string(hp_is_now)
	print_integer($s0)
	print_string(sym_minus)
	print_integer($t7)
	print_string(sym_equals)
	sub $s0, $s0, $t7	# Lower hero health
	print_integer($s0)
	
	print_newlines(2)
.end_macro

.macro print_hero_defeated_minion(%enemy)
	print_hero_w_space(1)
	print_strings(defeated, %enemy)
	print_integer($t4)
	print_string(sym_exclaim)
	print_newlines(2)
.end_macro

.macro print_hero_hp()
	print_string(hero_hp)
	print_integer($s0)
	print_newline()
.end_macro

.macro print_hero_str()
	print_string(hero_strength)
	print_integer($s1)
	print_newline()
.end_macro

.macro display_hero_stats()
	print_hero_hp()
	print_hero_str()
.end_macro

.macro hero_wins_battle()
	print_string(text_pad_dash)
	print_hero_w_space(1)
	print_strings(win_battle, text_pad_dash)
	print_newline()
.end_macro

.macro hero_wins()
	println("\nYou win! Congratulations!")
.end_macro

.macro hero_defeated_in_battle()
	print_string(text_pad_dash)
	print_hero_w_space(1)
	print_strings(lose_battle, text_pad_dash)
	print_newlines(2)
	println("GAME OVER")
.end_macro

# Program helper macros -------------------------------------
.macro get_rand_between(%lowerbound, %upperbound)
	get_random_int_registers(%lowerbound, %upperbound)
	move $t9, $a0
.end_macro

# Menu Macros -----------------------------------------------
.macro select_hero()
	println("Here are the characters.")
	println(" 1. Rogue")
	println(" 2. Paladin")
	println(" 3. Jackie Chan")
	print_newline()
	get_digit_from_user("Which character do you choose?: ", $t0)
	print_newline()
.end_macro

.macro select_path()
	println("\nTo get to The Castle, you must travel through either: ")
	println(" 1. The Forest")
	println(" 2. The Graveyard")
	print_newline()
	get_digit_from_user("Which path will you take?: ", $t0)
	print_newline()
.end_macro

.macro select_item()
	println("\nPlease choose a reward.")
	println(" 1. Healing Potion")
	println(" 2. Ring of Strength")
	print_newline()
	get_digit_from_user("Which item do you choose?: ", $t0)
	print_newline()
.end_macro

.macro wiz_fight_player_turn()
	print_strings(choose_action, choose_attack, choose_spell)
	print_newline()
	get_digit_from_user("What would you like to do?: ", $t0)
	print_newlines(2)
.end_macro

# Load player/enemy attributes ------------------------------
.macro load_hero_attr()
	lb $s0, HEALTH($t5)
	lb $s1, STRENGTH($t5)
	lb $s2, DAMAGE_LOW($t5)
	lb $s3, DAMAGE_HIGH($t5)
.end_macro

.macro load_hero_choice()
		beq $t0, 1, ROGUE	# Load Rogue
		beq $t0, 2, PALADIN	# Load Paladin
		beq $t0, 3, CHAN	# Load Jackie Chan
		
	ROGUE:
		la $t5, rogue_stats
		load_hero_attr()
		la $t6, rogue
		print_strings(choicemessage, rogue)
		j DONE_HERO_LOAD

	PALADIN:
		la $t5, paladin_stats
		load_hero_attr()
		la $t6, paladin
		print_strings(choicemessage, paladin)
		j DONE_HERO_LOAD
		
	CHAN:
		la $t5, jchan_stats
		load_hero_attr()
		la $t6, jchan
		print_strings(choicemessage, jchan)
		j DONE_HERO_LOAD

	DONE_HERO_LOAD:
		print_newline()
.end_macro

.macro load_enemy_attr()
	lb $s4, HEALTH($t5)
	lb $s5, STRENGTH($t5)
	lb $s6, DAMAGE_LOW($t5)
	lb $s7, DAMAGE_HIGH($t5)
.end_macro

.macro load_path_choice()
		beq $t0, 1, FOREST		# Load Goblin
		beq $t0, 2, GRAVEYARD	# Load Skeleton

	FOREST:
		la $t5, goblin_stats
		load_enemy_attr()
		print_strings(choicemessage, path_forest)
		j DONE_PATHLOAD
		
	GRAVEYARD:
		la $t5, skel_stats
		load_enemy_attr()
		print_strings(choicemessage, path_graveyard)
		j DONE_PATHLOAD

	DONE_PATHLOAD:
		print_newlines(2)
.end_macro

.macro load_wizard_attr()
	la $t5, wizard_stats
	load_enemy_attr()
.end_macro

# Fight Simulations -----------------------------------------
.macro hero_attacks(%enemy)
	move $t8, $s1
	get_rand_between($s2, $s3)
	add $t7, $t8, $t9
	print_hero_attacks(%enemy)
.end_macro
	
.macro enemy_attacks(%enemy)
	move $t8, $s5
	get_rand_between($s6, $s7)
	add $t7, $t8, $t9
	print_enemy_attacks(%enemy)
.end_macro

.macro fight_minion(%minion_type)
	print_versus_msg(%minion_type)
		
	HERO_ATTACKS_ENEMY:
		hero_attacks(%minion_type)
		blez $s4, ENEMY_DEFEATED

	ENEMY_ATTACKS_HERO:
		enemy_attacks(%minion_type)
		blez $s0, HERO_DEFEATED
		j HERO_ATTACKS_ENEMY

	ENEMY_DEFEATED:
		print_hero_defeated_minion(%minion_type)
		# Minion is dead, remember to reset health!
.end_macro

.macro reset_minion_health()
	li $s4, MINION_MAX_HEALTH
.end_macro

.macro fight_on_path()
		move $t4, $0	# $t4 acts as loop counter
		beq $t0, 1, FIGHT_GOBLINS	# 1 means forest path
		beq $t0, 2, FIGHT_SKELETONS	# 2 means graveyard path
		
	FIGHT_GOBLINS:
		addi $t4, $t4, 1
		fight_minion(goblin)
		reset_minion_health()	# Reset goblin health
		bne $t4, 3, FIGHT_GOBLINS	# Fight 3 goblins
		j END_ENCOUNTER
		
	FIGHT_SKELETONS:
		addi $t4, $t4, 1
		fight_minion(skeleton)
		reset_minion_health()	# Reset skeleton health
		bne $t4, 5, FIGHT_SKELETONS	# Fight 5 skeletons
		j END_ENCOUNTER

	END_ENCOUNTER:
		hero_wins_battle()
		print_newline()
.end_macro

# Wizard fight scene ---------------------------------------
.macro player_attacks_wiz()
		beq $t0, 1, ATTACK_WIZ
		beq $t0, 2, ATTEMPT_SPELL_CAST

	ATTACK_WIZ:
		hero_attacks(wizard)
		j END_ATTACK

	ATTEMPT_SPELL_CAST:
		get_digit_from_user("Enter your guess: ", $t0)
		print_newline()

		get_random_int(1, 5)
		beq $a0, $t0, SUCCESS
		print_strings(incorrect, newline)
		j END_ATTACK

	SUCCESS:
		println("Correct!\n")

		move $s4, $0

		print("The ")
		print_hero_w_space(0)
		print_string(spell_cast)
		print_newline()
		j END_ATTACK

	END_ATTACK:
		move $t0, $0
.end_macro

.macro fight_wizard()
		print_versus_wizard()

	HERO_ATTACKS_WIZARD:
		wiz_fight_player_turn()
		player_attacks_wiz()
		blez $s4, WIZARD_DEFEATED

	WIZARD_ATTACKS_HERO:
		enemy_attacks(wizard)
		blez $s0, HERO_DEFEATED
		j HERO_ATTACKS_WIZARD

	WIZARD_DEFEATED:
		hero_wins_battle()
.end_macro

# Item Effects ----------------------------------------------
.macro wear_ring_of_strength()
	addi $s1, $s1, 5
.end_macro

.macro drink_healing_potion()
	addi $s0, $s0, 10
.end_macro

.macro load_item_effect()
		beq $t0, 1, POTION
		beq $t0, 2, RING

	POTION:
		print_strings(choicemessage, hp_potion, newline)
		print("\nYour HP has increased to ")
		print_integer($s0)
		print_string(sym_plus)
		li $t8, 10
		print_integer($t8)
		print_string(sym_equals)
		
		drink_healing_potion()
		
		print_integer($s0)
		print_string(sym_exclaim)
		j ITEM_LOADED

	RING:
		print_strings(choicemessage, strength_ring, newline)
		print("\nYour Strength has increased to ")
		print_integer($s1)
		print_string(sym_plus)
		li $t8, 5
		print_integer($t8)
		print_string(sym_equals)
		
		wear_ring_of_strength()
		
		print_integer($s1)
		print_string(sym_exclaim)
		j ITEM_LOADED

	ITEM_LOADED:
		print_newlines(2)
.end_macro

# -----------------------------------------------------------

# Memory address offsets
.eqv	HEALTH			0 
.eqv	STRENGTH		1
.eqv	DAMAGE_LOW		2
.eqv	DAMAGE_HIGH		3

.eqv	MINION_MAX_HEALTH	25

.data
	# Heroes
	rogue:		.asciiz "Rogue"
	paladin:	.asciiz "Paladin"
	jchan:		.asciiz "Jackie Chan"
	
	# Enemies
	goblin:		.asciiz "Goblin "
	skeleton:	.asciiz "Skeleton "
	wizard:		.asciiz "Wizard "
	evil_wizard:	.asciiz	"The Evil Wizard"
	
	# Fields = {HP, Strength, Weapon_Damage_Low, Weapon_Damage_High}
	rogue_stats:	.byte 55, 8, 1, 4
	paladin_stats:	.byte 35, 14, 3, 7
	jchan_stats:	.byte 45, 10, 2, 6
	
	goblin_stats:	.byte 25, 4, 2, 6
	skel_stats:		.byte 25, 3, 1, 4
	wizard_stats:	.byte 40, 8, 4, 10
	
	# Wizard Fight Menu
	choose_action:	.asciiz	"Choose your action:\n"
	choose_attack:	.asciiz	"1. Attack\n"
	choose_spell:	.asciiz	"2. Attempt Spell Cast\n"
	
	# Game Messages
	win_battle:		.asciiz	"wins the battle!"
	lose_battle:	.asciiz	"is defeated in battle!"
	text_pad_star:	.asciiz	"***"
	text_pad_dash:	.asciiz	"--"
	text_versus:	.asciiz	"vs "
	sym_equals:		.asciiz	" = "
	sym_plus:		.asciiz	" + "
	sym_minus:		.asciiz	" - "
	sym_exclaim:	.asciiz	"!"
	sym_space:		.asciiz	" "
	startmessage:	.asciiz "Adventure Game - Start!\n\n"
	goalmessage:	.asciiz "\nThe Evil Wizard must be defeated! He is in the castle."
	choicemessage:	.asciiz "\nYou chose: "
	path_forest:		.asciiz	"Forest "
	path_graveyard:		.asciiz	"Graveyard "
	enter_msg_forest:		.asciiz	"Once you enter The Forest, you encounter 3 Goblins! "
	enter_msg_graveyard:	.asciiz	"Once you enter The Graveyard, you encounter 5 Skeletons! "
	battletime:	.asciiz	"Time for battle!\n"
	attack_msg:	.asciiz	"attacks with ATK"
	hp_is_now:	.asciiz	"HP is now "
	defeated:		.asciiz	"defeated "
	hero_hp:		.asciiz	"Your HP is: "
	hero_strength:	.asciiz	"Your strength is: "
	hp_potion:		.asciiz	"Healing Potion"
	strength_ring:	.asciiz	"Ring of Strength"
	incorrect:	.asciiz	"Incorrect ~.~\n"
	spell_fail:	.asciiz	"The spell cast failed! No damage is dealth to the wizard.\n"
	spell_cast:	.asciiz	"'s spell is cast successfully! The Wizard's HP is now 0!\n"
	
.text

MAIN:
	print_string(startmessage)	# Welcome user
	
	select_hero()				# Hero selection
	load_hero_choice()
	
	print_string(goalmessage)	# Progress Story
	
	select_path()				# Path selection
	load_path_choice()
	
	fight_on_path()				# Fight Simulation
	display_hero_stats()
	
	select_item()				# Player chooses reward
	load_item_effect()
	
	println("You have now reached the Castle! Time to battle the Evil Wizard!\n")

	load_wizard_attr()			# Battle Wizard
	fight_wizard()

	j HERO_VICTORIOUS

HERO_DEFEATED:
	hero_defeated_in_battle()
	EXIT

HERO_VICTORIOUS:
	hero_wins()
	EXIT
