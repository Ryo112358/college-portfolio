/* Program plays rock, paper, scissors w/ one human player
 * @author Ryo112358 (Jan 2018)
 */

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <time.h>

void read_line(const char* message, char* buffer, int length)
{
	printf(message);
	fgets(buffer, length, stdin);
	if(strlen(buffer) != 0)
		buffer[strlen(buffer) - 1] = 0;
}

int streq_nocase(const char* a, const char* b)
{
	for(; *a && *b; a++, b++)
		if(tolower(*a) != tolower(*b))
			return 0;

	return *a == 0 && *b == 0;
}

// Helper Functions ----------------------------------------------
int random_gesture(int low, int high)
{
	return rand() % (high-low+1) + low;
}

// Paper=0, Rock=1, Scissors=2
int gesture_to_int(const char* gesture)
{
	char paper[10] = "paper";
	char rock[10] = "rock";
	char scissors[10] = "scissors";
	char *gestures[] = {paper, rock, scissors};

	for(int i = 0; i < 3; i++)
	{
		if(streq_nocase(gesture, gestures[i]))
			return i;
	}
	
	return -1;
}

// Returns: -1=Computer, 1=Human, 0=Draw
int judge_round(int comp_choice, int human_choice)
{
	if(comp_choice == 0)
	{
		if(human_choice == 1)
			return -1;
		if(human_choice == 2)
			return 1;
		if(human_choice == 0)
			return 0;
	}
	else if(comp_choice == 1)
	{
		if(human_choice == 2)
			return -1;
		if(human_choice == 0)
			return 1;
		if(human_choice == 1)
			return 0;
	}
	else if(comp_choice == 2)
	{
		if(human_choice == 0)
			return -1;
		if(human_choice == 1)
			return 1;
		if(human_choice == 2)
			return 0;
	}
	return 0;
}

void decide_winner(int comp_score, int human_score)
{
	if(human_score > comp_score)
		printf("\nYou win the tournament!");
	else
		printf("\nYou lose the tournament :(");
}

int play_again()
{
	char userinput[50];

	read_line("  Play again (yes/no)? ", userinput, sizeof(userinput));

	if(userinput[0] == 'y' || userinput[0] == 'Y')
		return 1;

	return 0;
}

int main()
{
	srand((unsigned int)time(NULL));

	int score[] = {0,0}; // {Computer, Human}
	int round; // Current round
	char userinput[50];
	int comp = -1, human = -1, round_result = 0;

	char paper[10] = "paper";
	char rock[10] = "rock";
	char scissors[10] = "scissors";
	char *gestures[] = {paper, rock, scissors};

	printf("\n-------------- Welcome to RPS League! --------------\n");
	printf("Valid gestures: 'rock', 'paper', 'scissors'\n");
	printf("----------------------------------------------------\n");
	
	do {
		// Reset variables for new game
		round = 1;
		score[0] = 0;
		score[1] = 0;

		while(score[0] != 3 && score [1] != 3)
		{
			printf("\nRound %d! ", round);

			// Get human gesture selection
			read_line("What's your choice? ", userinput, sizeof(userinput));

			do {
				human = gesture_to_int(userinput);

				if(human == -1)
					read_line("Select a valid gesture: ", userinput, sizeof(userinput));
			} while(human == -1);

			// Get computer gesture selection
			comp = random_gesture(0,2);
			printf("The computer chooses %s. ", gestures[comp]);

			//Update score
			round_result = judge_round(comp, human);

			if(round_result == -1)
			{
				printf("You lose this round! ");
				score[0]++;
			}
			else if(round_result == 1)
			{
				printf("You win this round! ");
				score[1]++;
			}
			else {
				printf("It's a tie! ");
			}

			printf("You: %d Computer: %d\n", score[1], score[0]);

			//Reset variables & increment round
			comp = -1, human = -1, round_result = 0;
			round++;
		}

		decide_winner(score[0], score[1]);

	} while(play_again());

	return 0;
}
