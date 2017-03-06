'''
Weatherbowl CSV Script in Python3 minified
 - Usage: python weatherbowl-csv.min.py <team-name> <team-name> <year> <rain, snow>
 - Example: python weatherbowl-csv.min.py raid steel 2005 rain
Weather data retrieved from WunderGround
@author: Ryo112358
'''
#!/usr/bin/python
import sys
import pandas as pd
print()
sub_teamA = str(sys.argv[1]).lower()
sub_teamB = str(sys.argv[2]).lower()
year = str(sys.argv[3])
precType = str(sys.argv[4]).lower()
NFL = pd.read_csv("NFL_data.csv")
clubNames = NFL["Club"].str.lower()
def findTeamIndex(substring):
    teamName = next((s for s in clubNames if substring in s), None)
    if (teamName == 'None'):
        print("Team name not found... :(")
        exit(1)
    return clubNames[clubNames == teamName].index[0];
indexTeamA = findTeamIndex(sub_teamA)
indexTeamB = findTeamIndex(sub_teamB)
def returnURL(sub_url):
    teamURL = url_part1 + sub_url + url_part3
    # print(teamURL)
    return teamURL;
url_part1 = "https://www.wunderground.com/history/airport/"
url_part3 = "/1/1/CustomHistory.html?dayend=31&monthend=12&yearend=2010&req_city=&req_state=&req_statename=&reqdb.zip=&reqdb.magic=&reqdb.wmo=&format=1"
url_part2A = NFL.iloc[indexTeamA, 2] + "/" + year
url_part2B = NFL.iloc[indexTeamB, 2] + "/" + year
url_teamA = returnURL(url_part2A)
url_teamB = returnURL(url_part2B)
weatherdf_A = pd.read_csv(url_teamA, sep='\\s*,\\s*', engine='python')
weatherdf_A_trimmed = weatherdf_A.loc[:,["PrecipitationIn", "Events"]]
try:
    weatherdf_A_trimmed['Events'] = weatherdf_A['Events'].str.lower()
except AttributeError:
    print("The " + NFL.iloc[indexTeamA, 2] + " airport has no events data for this year!")
    exit(1)
weatherdf_B = pd.read_csv(url_teamB, sep='\\s*,\\s*', engine='python')
weatherdf_B_trimmed = weatherdf_B.loc[:,["PrecipitationIn", "Events"]]
try:
    weatherdf_B_trimmed['Events'] = weatherdf_B_trimmed['Events'].str.lower()
except AttributeError:
    print("The " + NFL.iloc[indexTeamB, 2] + " airport has no events data for this year!")
    exit(1)
teamA_Total = 0
teamB_Total = 0
daysInYear = int(weatherdf_A_trimmed.size/2)
for day in range(daysInYear):
    if type(weatherdf_A_trimmed.iloc[day, 1]) != float:
        if precType in weatherdf_A_trimmed.iloc[day, 1]:
            if weatherdf_A_trimmed.iloc[day, 0] == 'T':
                continue
            else:
                teamA_Total += float(weatherdf_A_trimmed.iloc[day, 0])
for day in range(daysInYear):
    if type(weatherdf_B_trimmed.iloc[day, 1]) != float:
        if precType in weatherdf_B_trimmed.iloc[day, 1]:
            if weatherdf_B_trimmed.iloc[day, 0] == 'T':
                continue
            else:
                teamB_Total += float(weatherdf_B_trimmed.iloc[day, 0])
winner = 0;
winPercentage = 0;
winnerZero = False;
indexTeamA = int(indexTeamA)
indexTeamB = int(indexTeamB)
if teamA_Total == teamB_Total:
    winner = 0
elif teamA_Total > 0 and teamB_Total == 0:
    winner = indexTeamA
    if winner == 0:
        winnerZero = True
    winPercentage = 'Infinity'
elif teamB_Total > 0 and teamA_Total == 0:
    winner = indexTeamB
    if winner == 0:
        winnerZero = True
    winPercentage = 'Infinity'
elif teamA_Total > teamB_Total:
    winner = indexTeamA
    if winner == 0:
        winnerZero = True
    winPercentage = float(100 * ((teamA_Total/teamB_Total) - 1))
else:
    winner = indexTeamB
    if winner == 0:
        winnerZero = True
    winPercentage = float(100 * ((teamB_Total/teamA_Total) - 1))
print("YEAR: " + year)
print("TYPE: " + precType)
print("TEAM-1: " + NFL.iloc[indexTeamA, 0])
print("CITY-1: " + NFL.iloc[indexTeamA, 2])
print("PRECIP-1: " + str(round(teamA_Total,2)) + " inches")
print("TEAM-2: " + NFL.iloc[indexTeamB, 0])
print("CITY-2: " + NFL.iloc[indexTeamB, 2])
print("PRECIP-2: " + str(round(teamB_Total,2)) + " inches")
if winner == 0 and winnerZero == False:
    print("WINNER: NO WINNER")
    print("PERCENT: N/A")
else:
    print("WINNER: " + NFL.iloc[winner, 0])
    print("PERCENT: " + str(winPercentage) + " %")
