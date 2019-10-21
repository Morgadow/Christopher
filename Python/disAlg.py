import sys, os, copy

os.system("cls")
print("--- Sicker Ressourcen Verteil Algo ---")

vills = []
vills.append([[20, 50, 30],25])
vills.append([[20, 50, 50],60])
vills.append([[100, 50, 50],40])
vills.append([[40, 40, 80],35])
vills.append([[20, 100, 60],35])
vills.append([[40, 25, 75],22])
vills.append([[50, 10, 95],30])

villsOld = copy.deepcopy(vills)

# ---- Status quo ---- #
carrier = []
for counter in range(len(vills)):
    carrier.append(vills[counter][1])

# ---- Global ---- #   
globalSum = [0,0,0]
for counter in range(3):
    for village in vills:
        globalSum[counter] = globalSum[counter] + village[0][counter]
print("\nSumme Global:",globalSum)
if globalSum[0] >=1.5*globalSum[1] or globalSum[0] >= 1.5*globalSum[2]:
    print("Globaler Holzüberschuss!")
if globalSum[1] >=1.5*globalSum[2] or globalSum[1] >= 1.5*globalSum[0]:
    print("Globaler Steinüberschuss!")
if globalSum[2] >=1.5*globalSum[0] or globalSum[2] >= 1.5*globalSum[1]:
    print("Globaler Eisenüberschuss!")
    
# ---- Durchschnitt Dorf ---- #
means = []
for vill in vills:
    means.append(int(sum(vill[0])/3))
    
# ---- Abweichungen ---- #
diffs = []
counter = 0
for vill in vills:
    curr = [0,0,0]
    for res in range(3):
        curr[res] = vill[0][res]-means[counter]
    diffs.append(curr)
    counter += 1
    
# ---- Einzelne Diffs auslagern ---- #
wood = []
stone = []
iron = []
for counter in range(len(diffs)):
    wood.append(diffs[counter][0])
    stone.append(diffs[counter][1])
    iron.append(diffs[counter][2])
ressis = [wood,stone,iron]

# ---- Verteilen ---- #
for counter in range(len(vills)):
# for counter in range(50):
    
    # Durschnitt Dörfer
    means = []
    for vill in vills:
        means.append(int(sum(vill[0])/3))
        
    # Abweichungen innerhalb
    diffs = []
    counter = 0
    for vill in vills:
        curr = [0,0,0]
        for res in range(3):
            curr[res] = vill[0][res]-means[counter]
        diffs.append(curr)
        counter += 1
    
    # Einzelne Rohstoffe extrahieren
    wood = []
    stone = []
    iron = []
    for counter in range(len(diffs)):
        wood.append(diffs[counter][0])
        stone.append(diffs[counter][1])
        iron.append(diffs[counter][2])
    ressis = [wood,stone,iron]

    for res in range(3):
    
        maxIn = ressis[res].index(max(ressis[res]))
        minIn = ressis[res].index(min(ressis[res]))

        sent = min(abs(max(ressis[res])),abs(min(ressis[res])),carrier[maxIn])
            
        # max, min und händler anpassen
        ressis[res][maxIn] = ressis[res][maxIn] - sent
        ressis[res][minIn] = ressis[res][minIn] + sent
        vills[maxIn][0][res] = vills[maxIn][0][res] - sent
        vills[minIn][0][res] = vills[minIn][0][res] + sent
        carrier[maxIn] = carrier[maxIn] - sent
        
        
# ---- Verteilt ---- #
print("\nDavor:")
counter = 0
for village in villsOld:
    print("Dorf ",counter+1,":",village[0])
    counter += 1
    
means = []
for vill in villsOld:
    means.append(int(sum(vill[0])/3))
evalDiff = 0
for index, entry in enumerate(villsOld):
    for res in entry[0]:
        evalDiff = evalDiff + abs(res-means[index])    
print("Abweichungsgrad:",evalDiff)
carr = []
for counter in range(len(vills)):
    carr.append(vills[counter][1])
print("Händler:",carr)

print("\nDanach:")
counter = 0
for village in vills:
    print("Dorf ",counter+1,":",village[0])
    counter += 1
    
means = []
for vill in vills:
    means.append(int(sum(vill[0])/3))
evalDiff = 0
for index, entry in enumerate(vills):
    for res in entry[0]:
        evalDiff = evalDiff + abs(res-means[index])    
print("Abweichungsgrad:",evalDiff)
print("Händler:",carrier)


    

        


