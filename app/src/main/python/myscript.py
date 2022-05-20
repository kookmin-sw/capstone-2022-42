from os.path import dirname, join
import pandas as pd
from sklearn.preprocessing import MinMaxScaler#, StandardScaler, RobustScaler,
from scipy.spatial import distance

def main(x,y,z,u,w):
    dt = join(dirname(__file__), "mountain.json")
    #/Users/songyonguk/StudioProjects/capstone-2022-42/app
    df = pd.read_json(dt,orient = 'records')
    sayongja = [x,y,z]
    usercod = [u,w]

    #minmax scaler
    minmaxdf = df[['PMNTN_LT','PMNTN_UPPL','PMNTN_GODN']]
    min_max_scalcer = MinMaxScaler()
    fitted = min_max_scalcer.fit_transform(minmaxdf)
    er = pd.DataFrame(fitted)
    er['INDEX'] = df['INDEX']

    #GPS range
    gurry = 0.2

    #euclidean distance measuring
    eulist = [[]]
    def euclid(useri):
        for i in range(len(er)):
            eulist.append([i+1, distance.euclidean((useri[0]*0.000085, useri[1]*0.0004805,useri[2]*0.000686), (er.iloc[i][0], er.iloc[i][1], er.iloc[i][2]))])

    euclid(sayongja[0:3])

    eulist = eulist[1:]
    eulist.sort(key=lambda x:x[1])

    latlon = []

    df['lat'] = df.START_PNT.str.split(" ").str[0].astype(float)
    df['log'] = df.START_PNT.str.split(" ").str[1].astype(float)

    condition = ((df.lat > usercod[0] - gurry) & (df.lat < usercod[0] + gurry)) & ((df.log > usercod[1] - gurry) & (df.log < usercod[1] + gurry))
    userlocdf = df[condition]

    cnt = 0
    i = 0

    #recommendation list
    finallist = [1,2]

    while cnt < 30:
        try:
            if eulist[i][0] in userlocdf["INDEX"]:
                a = userlocdf[["MNTN_NM","PMNTN_NM"]].loc[eulist[i][0]-1]
                errrr = a.values.tolist()
                if errrr != finallist[-1]:
                    finallist.append(errrr)
                    cnt +=1
            i +=1
        except:
            break
    finallist = finallist[2:]
    return finallist

# #dummy user data input
# sayongja = (0.02, 2, 3)
# usercod = [126.9, 37.5]
# main(sayongja, usercod)




