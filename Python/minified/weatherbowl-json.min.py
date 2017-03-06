'''
Weatherbowl JSON Script in Python3 minified
 - Usage: python weatherbowl-json.min.py <team-name> <team-name> <year> <snow>
 - Example: python weatherbowl-json.min.py patriot steel 2009 snow
IMPORTANT: Script requires access to WunderGround API (See line 20)
@author: Ryo112358
'''
#!/usr/bin/python
import sys
import pandas as pd
import requests
from pandas.core.common import is_number
import calendar
sub_teamA = str(sys.argv[1]).lower()
sub_teamB = str(sys.argv[2]).lower()
year = str(sys.argv[3])
precType = str(sys.argv[4]).lower()
if precType != 'snow':
    print("Program only provides SNOWFALL data (for first three months). Continuing...")
# Set variable below as your WunderGround access key
my_WG_key = "YOUR_KEY_HERE"
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
# http://api.wunderground.com/api/INSERT_YOUR_KEY/history_20100101/q/KPIT.json
url_part1 = "http://api.wunderground.com/api/" + my_WG_key + "/history_" + year + "0"
url_part3 = "/q/"
url_part5 = ".json"
url_part4A = NFL.iloc[indexTeamA, 2]
url_part4B = NFL.iloc[indexTeamB, 2]
def returnURL(month, airport):
    lastDay = calendar.monthrange(int(year), month)[1]
    teamURL = url_part1 + str(month) + str(lastDay) + url_part3 + airport + url_part5
    return teamURL;
def findTeamSnowfall(airport):
    totalSnow = 0.0
    monthlySnowfall = 0.0
    for month in range (1,2):
        url_team = returnURL(month, airport)
        f = requests.get(url_team)
        parsed_json = f.json()
        monthlySnowfall = parsed_json['history']['dailysummary'][0]['monthtodatesnowfalli']
        try:
            monthlySnowfall = float(monthlySnowfall)
        except ValueError:
            continue
        if is_number(monthlySnowfall):
            totalSnow += float(monthlySnowfall)
        else:
            continue
    return totalSnow;
teamA_Total = findTeamSnowfall(url_part4A)
teamB_Total = findTeamSnowfall(url_part4B)
winner = 0
winPercentage = 0
winnerZero = False
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
print()
print("------------------------------")
print("VERSION: 1 MONTH")
print()
print("YEAR: " + year)
print("TYPE: Snow")
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
