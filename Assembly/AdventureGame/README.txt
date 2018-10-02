Adventure Game by Pulkit (@Ryo112358)

Section 1. Describe procedures/macros/constants
Section 2. Method for storing player and enemy attributes
Section 3. Known bugs (None)

Section 3 --------------------------------------------------

	No known bugs in code.

Section 2 --------------------------------------------------

	Refer to register key below. Since there were exactly 
	8 attributes which needed to be used at any time, the 
	8 $s# registers were used to hold these values.

	Printing the name attribute was challenging. For the 
	hero, a macro was created which looked at the hero 
	strength to determine which of the 3 was in play.

Section 1 --------------------------------------------------

REGISTER KEY
	################# = Miscellaneous

(Rogue, Paladin, or Chan)
$s0 – Hero HP
$s1 – Hero Strength
$s2 – Hero Low Wpn Dmg
$s3 – Hero High Wpn Dmg

(Skeleton, Goblin, or Wizard)
$s4 – Enemy HP
$s5 – Enemy Strength
$s6 – Enemy Low Wpn Dmg
$s7 – Enemy High Wpn Dmg

$t0 – User input
$t1 – #################
$t2 – #################
$t3 – #################
$t4 – Loop counter
$t5 – Temporary address storage

$t6 – #################
$t7 – Calculation result
$t8 – Calc operand
$t9 – Calc operand/Generated random number


CONSTANTS
.eqv Health			0 
.eqv Strength		1
.eqv Damage_Low		2
.eqv Damage_High	3

Just simple offsets used to load player & enemy stats

PROCEDURES
 - Two procedures in main body: MAIN & HERO_DEFEATED
 - Many macros have supporting sub-procedures to support their specific purpose

MACROS HEADERS PER FILE
	adventure-game.asm
	 # Print macros -----------------------------
	 + print_hero_w_spaces(%bool)
	 + print_versus_msg(%enemy)
	 + print_versus_wizard()
	 + print_hero_attacks(%enemy)
	 + print_enemy_attacks(%enemy)
	 + print_hero_defeated_minion(%enemy)
	 + print_hero_hp()
	 + print_hero_str()
	 + display_hero_stats()
	 + hero_wins()
	 + game_over()

	 # Program helper macros --------------------
	 + get_rand_between(%lowerbound, %upperbound)
	 + hero_defeated_in_battle()
	 + hero_wins_battle()

	 # Menu macros ------------------------------
	 + select_hero()
	 + select_path()
	 + select_item()
	 + wiz_fight_player_turn()

	 # Load player/enemy attributes -------------
	 + load_hero_attr()
	 + load_hero_choice()
	 + load_enemy_attr()
	 + load_path_choice()
	 + load_wizard_attr()

	 # Fight Simulations ------------------------
	 + hero_attacks(%enemy)
	 + enemy_attacks(%enemy)
	 + fight_goblin()
	 + fight_skeleton()
	 + fight_on_path()
	 + player_attacks_wiz()
	 + fight_wizard()

	 # Item effects -----------------------------
	 + ring_of_strength()
	 + healing_potion()
	 + load_item_effect()

	general-macros.asm
	 + print_string(%str)
	 + print_strings(%str1, %str2)
	 + print_strings(%str1, %str2, %str3)
	 + print_integer(%register)
	 + print_newline()
	 + print_newlines(%count)

	 + print() - Equivalent to Java's System.out.print()
	 + println() - Equivalent to Java's System.out.println()

	 + get_int_from_user(%message, %register)
	 + get_digit_from_user(%message, %register)

	 + EXIT - Stop program

	generate-random-number.asm
	 + get_time()
	 + generate_seed()

	 + get_random_int(%upperbound)
	 + get_random_int(%lowerbound, %upperbound)
	 + get_random_int_registers(%lowerbound, %upperbound)

	 + announce_random(%upperbound)
	 + print_random_result(%upperbound)
	 + announce_random(%lowerbound, %upperbound)
	 + print_random_result(%lowerbound, %upperbound)

CHALLENGES
 - The string printing aspect was by far the most challenging to do efficiently since there is no simple way to store variables
 - A special method was made for printing the hero name due to limited registers
 	- In retrospect, there were enough unused registers so one could've been used to permanently hold the hero string address

POTENTIAL IMPROVEMENTS
 - Keep the hero string address in an unused register (e.g. $t6) and print as needed (less operations --> slightly improved runtime)
 - Look through macros for those with similar functions that can be merged
