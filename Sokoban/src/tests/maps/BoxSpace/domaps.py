import pickle
f = open("all-slc.txt","r")
count =0
b = True
f.readline()
while (b):
    try:
        c = True
        text=""
        while (c):     
            temp = f.readline()
            if (temp[0]==";"):
                c=False
                count+=1
            else:
                text += temp
                print temp,
        g = open("map"+str(count)+".txt","w")
        g.write(text)
        g.close()
    except:
        b = False
f.close()
