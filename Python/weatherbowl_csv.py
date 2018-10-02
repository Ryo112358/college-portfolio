'''
Created Jan 23, 2017

@author: Ryo112358
'''

#!/usr/bin/python

import sys
import pandas as pd

# Command line base variables --------------------------------

sub_teamA = sys.argv[1]
sub_teamB = sys.argv[2]
year = str(sys.argv[3])
precType = sys.argv[4]

# print(sub_teamA, sub_teamB, year, precType)


# Eclipse testing variables-----------------------------------

# year = str(2010)
# precType = 'snow'
#
# sub_teamA = 'pit'
# sub_teamB = 'ta fal'

# Read NFL Teams File----------------------------------------
NFL = pd.read_csv("NFL_data.csv")
# print(NFL.head(5))
print()

clubNames = NFL["Club"].str.lower()
# print(clubNames)

# Find team index given a valid substring--------------------
def findTeamIndex(substring):

    teamName = next((s for s in clubNames if substring in s), None)
    if (teamName == 'None'):
        print("Team name not found... :(")
        exit(1)

    #print(teamName)

    return clubNames[clubNames == teamName].index[0];


# Find Team Indices---------------------------------------------

indexTeamA = findTeamIndex(sub_teamA.lower())

indexTeamB = findTeamIndex(sub_teamB.lower())


# Define URL Links for Data Mining-----------------------------
url_part1 = "https://www.wunderground.com/history/airport/"
url_part3 = "/1/1/CustomHistory.html?dayend=31&monthend=12&yearend=2010&req_city=&req_state=&req_statename=&reqdb.zip=&reqdb.magic=&reqdb.wmo=&format=1"

def returnURL(sub_url):
    teamURL = url_part1 + sub_url + url_part3
    return teamURL;

# Airport name is stored in column 3 (index 2) of dataframe
url_part2A = NFL.iloc[indexTeamA, 2] + "/" + year
url_part2B = NFL.iloc[indexTeamB, 2] + "/" + year
#print(url_part2A)

# Combine URL pieces into two links
url_teamA = returnURL(url_part2A);
url_teamB = returnURL(url_part2B);

# print(url_teamA)
# print(url_teamB)

# Retrieve weather data for both teams-------------------------
weatherdf_A = pd.read_csv(url_teamA, sep='\\s*,\\s*', engine='python', parse_dates=['EST'])
weatherdf_A_trimmed = weatherdf_A.loc[:,["PrecipitationIn", "Events"]]

try:
    weatherdf_A_trimmed['Events'] = weatherdf_A['Events'].str.lower()
except AttributeError:
    print("The " + NFL.iloc[indexTeamA, 2] + " airport has no events data for this year!")
    exit(1)

# print(weatherdf_A_trimmed)

weatherdf_B = pd.read_csv(url_teamB, sep='\\s*,\\s*', engine='python', parse_dates=['EST'])
weatherdf_B_trimmed = weatherdf_B.loc[:,["PrecipitationIn", "Events"]]
# print(weatherdf_B.head())

try:
    weatherdf_B_trimmed['Events'] = weatherdf_B_trimmed['Events'].str.lower()
except AttributeError:
    print("The " + NFL.iloc[indexTeamB, 2] + " airport has no events data for this year!")
    exit(1)

# print(weatherdf_A_trimmed.columns)
# print(weatherdf_B_trimmed.head())
# print(weatherdf_B_trimmed.index)
# print(weatherdf_A_trimmed.size)


# Add up team weather scores------------------------------------
teamA_Total = 0;
teamB_Total = 0;

daysInYear = int((weatherdf_A_trimmed.size/2))

for day in range(0, daysInYear):
    # print("Loop: " + str(day))
    if type(weatherdf_A_trimmed.iloc[day, 1]) != float:
        if precType in weatherdf_A_trimmed.iloc[day, 1]:
            if weatherdf_A_trimmed.iloc[day, 0] == 'T':
                continue
            else:
                teamA_Total += float(weatherdf_A_trimmed.iloc[day, 0])

# print(teamA_Total)


for day in range(0, daysInYear):
    # print("Loop: " + str(day))
    if type(weatherdf_B_trimmed.iloc[day, 1]) != float:
        if precType in weatherdf_B_trimmed.iloc[day, 1]:
            if weatherdf_B_trimmed.iloc[day, 0] == 'T':
                continue
            else:
                teamB_Total += float(weatherdf_B_trimmed.iloc[day, 0])

# print(teamB_Total)

# Decide winning team------------------------------------------
winner = 0;
winPercentage = 0;

if teamA_Total == 0 and teamB_Total == 0:
    winner = 0 # No winner
elif teamA_Total > 0 and teamB_Total == 0:
    winner = indexTeamA
    winPercentage = 'Infinity'
elif teamB_Total > 0 and teamA_Total == 0:
    winner = indexTeamB
    winPercentage = 'Infinity'
elif teamA_Total > teamB_Total:
    winner = indexTeamA
    winPercentage = float(100 * ((teamA_Total/teamB_Total) - 1))
else:
    winner = indexTeamB
    winPercentage = float(100 * ((teamB_Total/teamA_Total) - 1))

# Print Final Results------------------------------------------

# YEAR: 1850
# TYPE: snow
# TEAM-1: Pittsburgh Steelers
# CITY-1: KPIT
# PRECIP-1: 26.5
# TEAM-2: New England Patriots
# CITY-2: KOWD
# PRECIP-2: 25.0
# WINNER: Pittsburgh Steelers
# PERCENT: 6.00%


print("YEAR: " + year)
print("TYPE: " + precType)
print("TEAM-1: " + NFL.iloc[indexTeamA, 0])
print("CITY-1: " + NFL.iloc[indexTeamA, 2])
print("PRECIP-1: " + str(round(teamA_Total,2)) + " inches")
print("TEAM-2: " + NFL.iloc[indexTeamB, 0])
print("CITY-2: " + NFL.iloc[indexTeamB, 2])
print("PRECIP-2: " + str(round(teamB_Total,2)) + " inches")

if winner == 0:
    print("WINNER: NO WINNER")
    print("PERCENT: N/A")
else:
    print("WINNER: " + NFL.iloc[winner, 0])
    print("PERCENT: " + str(winPercentage) + " %")
