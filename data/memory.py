import numpy as np

def calc_overhead(unmonitored, monitored):
    # overhead
    diffs = np.subtract(monitored, unmonitored)

    # mean and STD
    media = np.mean(diffs)
    deviazione_standard = np.std(diffs)

    mean_monitored = np.mean(monitored)

    perc_overhead = media/mean_monitored *100


    return media, deviazione_standard, perc_overhead

def calc_max_perc(unmonitored, monitored):
    # overhead

    max_unmonitored = np.max(unmonitored)
    max_monitored = np.max(monitored)


    perc = min(max_unmonitored,max_monitored )/ max(max_unmonitored,max_monitored) 

    return perc * 100


# Esempio di utilizzo

toyapp = np.array([12.73, 6.55,6.55,8.55,9.55,10.55,10.55,10.55,11.55,11.55,11.55,12.55, 6.90,6.90,6.90,6.90,7.90,7.90,7.90,7.90,7.90,8.90,8.90,9.90,9.90,9.90,9.90,9.90,10.90,10.90,10.90,10.90,11.90,11.90,11.90,12.90,12.90,12.90,12.90,12.90, 6.90,6.90,6.90,7.90,7.90,7.90,7.90,8.90,8.90,8.90,8.90,8.90,9.90,9.90,9.90,10.90,10.90,10.90,10.90,11.90,11.90,11.90,11.90,11.90,12.90,12.90,12.90,6.90,6.90,6.90,6.90,7.90,7.90,7.90,8.90,8.90,8.90,8.90,8.90,9.90,9.90,9.90,10.90,10.90,10.90,10.90,11.90,11.90,11.90,12.90,12.90,12.90,12.90,12.90,6.92,6.92,6.92,7.92,7.92,7.92])

toyapp_oreo = np.array([11.83, 7.43, 7.43, 9.43, 10.43, 12.43, 12.43, 13.43, 13.43, 7.67, 7.67, 8.67, 8.67, 9.67, 10.67, 10.67, 11.67, 11.67, 12.67, 12.67, 13.67, 13.67, 7.72, 7.72, 8.72, 8.72, 9.72, 10.72, 10.72, 11.72, 11.72, 12.72, 12.72, 13.72, 13.72, 7.75, 7.75, 8.75, 8.75, 9.75, 10.75, 10.75, 11.75, 11.75, 12.75, 12.75, 13.75, 13.75, 7.78, 7.78, 7.78, 8.78, 9.78, 9.78, 10.78, 10.78, 11.78, 11.78, 12.78, 12.78, 13.78, 7.83, 7.83, 8.83, 8.83, 9.83, 9.83, 10.83, 10.83, 11.83, 12.83, 12.83, 13.83, 13.83, 7.84, 7.84, 7.84, 8.84, 8.84, 9.84, 9.84, 10.84, 10.84, 11.84, 12.84, 12.84, 13.84, 13.84, 7.87, 7.87, 8.87, 8.87, 9.87, 9.87, 10.87, 11.87, 11.87, 11.87, 11.87, 13.87])

books = np.array([56.22, 57.57, 59.73, 61.00, 65.49, 67.98, 68.53, 70.23, 72.06, 74.24, 78.18, 78.28, 78.37, 88.18, 88.71, 90.10, 92.03, 93.75, 94.21, 95.32, 97.43, 100.12, 101.57, 101.66, 101.85, 104.79, 105.46, 106.74, 108.67, 110.40, 111.61, 112.64, 114.34, 116.32, 117.72, 117.83, 118.02, 120.86, 121.39, 122.59, 124.54, 126.93, 16.68, 17.46, 19.04, 21.39, 23.97, 23.97, 23.98, 27.19, 27.21, 28.77, 30.33, 31.99, 32.79, 33.58, 35.14, 38.14, 39.71, 39.71, 39.72, 42.06, 42.84, 44.41, 45.98, 47.55, 49.09, 49.88, 52.22, 53.79, 55.40, 55.40, 55.40, 59.19, 59.19, 60.81, 62.37, 63.95, 64.73, 65.51, 68.58, 70.14, 71.71, 71.71, 71.71, 74.96, 74.96, 76.59, 78.15, 80.35, 81.15, 81.93, 84.28, 85.84, 87.46, 87.46, 87.46, 91.42, 92.20, 92.98])

books_oreo = np.array([63.45, 67.30, 68.91, 71.68, 73.46, 78.87, 83.55, 85.05, 87.21, 89.59, 94.86, 101.20, 102.11, 102.54,  9.04, 18.84, 20.43, 23.07, 26.96, 28.99, 30.64, 33.02, 36.88, 40.76, 41.54, 42.31, 45.44, 54.90, 56.49, 59.13, 62.99, 64.38, 65.94, 68.25, 72.19, 76.74, 77.69, 78.05, 81.49, 89.56, 91.17, 93.50, 97.45, 99.00, 100.60, 103.57, 106.66, 0.75392, 1.64, 2.52, 6.08, 14.16, 15.98, 19.17, 23.09, 24.49, 25.97, 28.47, 31.99, 36.76, 37.81, 38.17, 41.70, 49.67, 50.57, 53.21, 58.28, 59.18, 60.94, 63.59, 67.13, 70.75, 71.65, 72.53, 76.08, 83.99, 86.68, 89.14, 93.05, 94.46, 96.00, 1.47, 5.43, 6.55, 6.56, 10.04, 18.16, 20.16, 22.96, 27.55, 29.41, 30.35, 33.15, 36.88, 40.62, 41.56, 41.57, 46.17, 55.28, 56.97])

empedocle = np.array([152.69,167.47,169.52,170.70,171.60,149.38,150.99,158.28,159.51,163.98,165.21,174.83,176.06,184.16,185.51,135.09,137.30,141.20,142.86,152.76,155.42,157.09,158.31,159.49,160.32,167.93,169.22,173.88,175.19,177.71,179.01,185.06,129.64,133.73,135.61,141.82,143.00,144.24,146.98,153.39,155.85,157.01,158.19,159.36,160.54,168.12,170.34,174.41,175.59,177.93,179.11,185.64,129.40,133.13,134.71,140.83,142.28,144.90,145.68,151.66,153.19,155.44,156.20,157.69,159.14,166.61,167.37,171.86,172.62,174.88,177.31,183.29,184.06,130.77,132.60,138.76,140.34,142.07,142.95,149.02,151.46,153.19,154.08,154.94,155.82,163.62,164.50,167.98,170.39,173.00,173.87,179.94,181.52,184.99,186.53,135.65,137.32,139.20,140.84,146.48])

empedocle_oreo = np.array([192.22, 157.67, 179.53, 187.64, 149.88, 181.27, 182.54, 139.90, 174.11, 186.84, 196.73, 155.74, 169.38, 183.90, 146.55, 163.10, 181.86, 192.91, 169.02, 184.18, 191.44, 148.48, 178.78, 185.76, 149.69, 166.34, 166.30, 177.98, 205.45, 153.02, 150.06, 165.27, 221.75, 153.84, 176.66, 188.27, 208.09, 216.06, 169.72, 181.47, 188.58, 202.77, 145.11, 152.56, 173.13, 189.02, 160.77, 172.79, 167.59, 177.91, 149.33, 163.06, 188.83, 199.17, 217.99, 230.92, 148.03, 156.77, 187.43, 199.96, 205.56, 219.30, 249.64, 150.64, 172.19, 186.74, 245.25, 150.62, 241.64, 250.16, 208.65, 221.36, 238.94, 249.25, 157.47, 168.86, 188.10, 196.23, 226.06, 238.79, 244.46, 258.44, 176.88, 183.31, 204.77, 220.41, 163.35, 187.13, 249.55, 261.06, 239.64, 248.64, 199.59, 213.68, 224.76, 235.69, 254.43, 151.38, 172.11, 179.96])


toyapp_mean_overhead, toyapp_std_overhead, toyapp_perc_overhead = calc_overhead(toyapp, toyapp_oreo)


print("Toy app size " + str(toyapp.size))
print("Toy app size " + str(toyapp_oreo.size))

print("Books app size " + str(books.size))
print("Books app size " + str(books_oreo.size))

print("Empedocle app size " + str(empedocle.size))
print("Empedocle app size " + str(empedocle_oreo.size))

print("")

print(f"Toy app mean memory overhead: {toyapp_mean_overhead:.2f}")
print(f"Toy app std overhead: {toyapp_std_overhead:.2f}")
print(f"Toy app perc overhead: {toyapp_perc_overhead:.2f}")


books_mean_overhead, books_std_overhead, books_perc_overhead = calc_overhead(books, books_oreo)

print(f"Books app mean memory overhead: {books_mean_overhead:.2f}")
print(f"Books app std overhead: {books_std_overhead:.2f}")
print(f"Books app perc overhead: {books_perc_overhead:.2f}")


empedocle_mean_overhead, empedocle_std_overhead, empedocle_perc_overhead = calc_overhead(empedocle, empedocle_oreo)

print(f"Empedocle app mean memory overhead: {empedocle_mean_overhead:.2f}")
print(f"Empedocle app std overhead: {empedocle_std_overhead:.2f}")
print(f"Empedocle app perc overhead: {empedocle_perc_overhead:.2f}")



print("")
print("")

toyapp_max_perc = calc_max_perc(toyapp, toyapp_oreo)
print(f"toyapp app max perc overhead: {toyapp_max_perc:.2f}")
print("")

books_max_perc = calc_max_perc(books, books_oreo)
print(f"books app max perc overhead: {books_max_perc:.2f}")
print("")

empedocle_max_perc = calc_max_perc(empedocle, empedocle_oreo)
print(f"empedocle app max perc overhead: {empedocle_max_perc:.2f}")
print("")



print("############## TOY MIN MAX ############")

min_toy = np.min(toyapp)
min_toy_oreo = np.min(toyapp_oreo)

max_toy = np.max(toyapp)
max_toy_oreo = np.max(toyapp_oreo)

print(f"Toy app min memory: {min_toy:.2f}")
print(f"Toy app min memory with OREO: {min_toy_oreo:.2f}")

min_toy_diff = min_toy_oreo - min_toy
print(f"Toy app min diff memory : {min_toy_diff:.2f}")

print("")

print(f"Toy app max memory: {max_toy:.2f}")
print(f"Toy app max memory with OREO: {max_toy_oreo:.2f}")

max_toy_diff = max_toy_oreo - max_toy
print(f"Toy app max diff memory : {max_toy_diff:.2f}")

print("")

print("")
print("")


print("############## BOOKS MIN MAX ############")

min_books = np.min(books)
min_books_oreo = np.min(books_oreo)

max_books = np.max(books)
max_books_oreo = np.max(books_oreo)

print(f"books app min memory: {min_books:.2f}")
print(f"books app min memory with OREO: {min_books_oreo:.2f}")

min_books_diff = min_books_oreo - min_books
print(f"books app min diff memory : {min_books_diff:.2f}")

print("")


print(f"books app max memory: {max_books:.2f}")
print(f"books app max memory with OREO: {max_books_oreo:.2f}")

max_books_diff = max_books_oreo - max_books
print(f"books app max diff memory : {max_books_diff:.2f}")

print("")



print("")
print("")


print("############## EMPEDOCLE MIN MAX ############")

min_empedocle = np.min(empedocle)
min_empedocle_oreo = np.min(empedocle_oreo)


max_empedocle = np.max(empedocle)
max_empedocle_oreo = np.max(empedocle_oreo)

print(f"empedocle app min memory: {min_empedocle:.2f}")
print(f"empedocle app min memory with OREO: {min_empedocle_oreo:.2f}")

min_empedocle_diff = min_empedocle_oreo - min_empedocle
print(f"empedocle app min diff memory : {min_empedocle_diff:.2f}")

print("")

print(f"empedocle app max memory: {max_empedocle:.2f}")
print(f"empedocle app max memory with OREO: {max_empedocle_oreo:.2f}")

max_empedocle_diff = max_empedocle_oreo - max_empedocle
print(f"empedocle app max diff memory : {max_empedocle_diff:.2f}")

print("")

