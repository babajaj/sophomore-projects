# Projection Matrix Stencil Code
# Written by Eleanor Tursman, based on previous work by Henry Hu,
# Grady Williams, and James Hays for CSCI 1430 @ Brown and
# CS 4495/6476 @ Georgia Tech

import numpy as np
import math
from random import sample

# Returns the projection matrix for a given set of corresponding 2D and
# 3D points. 
# 'Points_2D' is nx2 matrix of 2D coordinate of points on the image
# 'Points_3D' is nx3 matrix of 3D coordinate of points in the world
# 'M' is the 3x4 projection matrix
def calculate_projection_matrix(Points_2D, Points_3D):
    # To solve for the projection matrix. You need to set up a system of
    # equations using the corresponding 2D and 3D points:
    #
    #                                                     [M11       [ u1
    #                                                      M12         v1
    #                                                      M13         .
    #                                                      M14         .
    #[ X1 Y1 Z1 1 0  0  0  0 -u1*X1 -u1*Y1 -u1*Z1          M21         .
    #  0  0  0  0 X1 Y1 Z1 1 -v1*X1 -v1*Y1 -v1*Z1          M22         .
    #  .  .  .  . .  .  .  .    .     .      .          *  M23   =     .
    #  Xn Yn Zn 1 0  0  0  0 -un*Xn -un*Yn -un*Zn          M24         .
    #  0  0  0  0 Xn Yn Zn 1 -vn*Xn -vn*Yn -vn*Zn ]        M31         .
    #                                                      M32         un
    #                                                      M33         vn ]
    #
    # Then you can solve this using least squares with the 'np.linalg.lstsq' operator.
    # Notice you obtain 2 equations for each corresponding 2D and 3D point
    # pair. To solve this, you need at least 6 point pairs. Note that we set
    # M34 = 1 in this scenario. If you instead choose to use SVD via np.linalg.svd, you should
    # not make this assumption and set up your matrices by following the 
    # set of equations on the project page. 
    #
    ##################
    # Your code here #
    num_points = Points_2D.shape[0]
    point_mat = np.zeros((num_points*2, 11))
    mat_2d = np.zeros((2*num_points, 1))

    for i in (range (num_points)):
        point_mat[2*i][0] = Points_3D[i][0]
        point_mat[2*i][1] = Points_3D[i][1]
        point_mat[2*i][2] = Points_3D[i][2]
        point_mat[2*i][3] = 1
        point_mat[2*i][8] = -(Points_2D[i][0])*(Points_3D[i][0])
        point_mat[2*i][9] = -(Points_2D[i][0])*(Points_3D[i][1])
        point_mat[2*i][10] = -(Points_2D[i][0])*(Points_3D[i][2])
        
        point_mat[2*i+1][4] = Points_3D[i][0]
        point_mat[2*i+1][5] = Points_3D[i][1]
        point_mat[2*i+1][6] = Points_3D[i][2]
        point_mat[2*i+1][7] = 1
        point_mat[2*i+1][8] = -(Points_2D[i][1])*(Points_3D[i][0])
        point_mat[2*i+1][9] = -(Points_2D[i][1])*(Points_3D[i][1])
        point_mat[2*i+1][10] = -(Points_2D[i][1])*(Points_3D[i][2])

        mat_2d[2*i][0] = Points_2D[i][0]
        mat_2d[2*i+1][0] = Points_2D[i][1]

    ##################
    # This M matrix came from a call to rand(3,4). It leads to a high residual.
    # Your total residual should be less than 1.
    Mpre = np.linalg.lstsq(point_mat, mat_2d)
    Mcol = Mpre[0] 
    M = np.array([[Mcol[0][0], Mcol[1][0], Mcol[2][0], Mcol[3][0]],
                  [Mcol[4][0], Mcol[5][0], Mcol[6][0], Mcol[7][0]],
                  [Mcol[8][0], Mcol[9][0], Mcol[10][0], 1]])

    return M

# Returns the camera center matrix for a given projection matrix
# 'M' is the 3x4 projection matrix
# 'Center' is the 1x3 matrix of camera center location in world coordinates
def compute_camera_center(M):
    ##################
    Q = M[0:3, 0:3]
    m4 = M[0:3, 3]
    Center = np.matmul(-np.linalg.inv(Q),m4)
    ##################

    # Replace this with the correct code
    # In the visualization you will see that this camera location is clearly
    # incorrect, placing it in the center of the room where it would not see all
    # of the points.

    return Center

# Returns the camera center matrix for a given projection matrix
# 'Points_a' is nx2 matrix of 2D coordinate of points on Image A
# 'Points_b' is nx2 matrix of 2D coordinate of points on Image B
# 'F_matrix' is 3x3 fundamental matrix
def estimate_fundamental_matrix(Points_a,Points_b):
    # Try to implement this function as efficiently as possible. It will be
    # called repeatly for part III of the project
    ##################
    # Your code here #
    ##################
    #                                          [f11       [ 0
    #                                           f12         0
    #                                           f13         0
    #[uu' vu' u'  uv'  vv'  v' u v 1            f21         .
    #                                           f22         .
    #  .  .  .  . .  .     .      .          *  f23   =     .
    #  Xn Yn 1  0  0  0 -un*Xn -un*Yn       
    #  0  0  0 Xn Yn 1 -vn*Xn -vn*Yn]           f31         .
    #                                           f32         0
    #                                           f33         0 ]

    num_points = Points_a.shape[0]
    point_mat = np.zeros((num_points, 9))
    mat_2d = np.zeros((num_points, 1))

    for i in (range (num_points)):
        point_mat[i][0] = Points_a[i][0]*Points_b[i][0]
        point_mat[i][1] = Points_a[i][1]*Points_b[i][0]
        point_mat[i][2] = Points_b[i][0]
        point_mat[i][3] = Points_a[i][0]*Points_b[i][1]
        point_mat[i][4] = Points_a[i][1]*Points_b[i][1]
        point_mat[i][5] = Points_b[i][1]
        point_mat[i][6] = Points_a[i][0]
        point_mat[i][7] = Points_a[i][1]
        point_mat[i][8] = 1
    

    U, S, Vh = np.linalg.svd(point_mat)
    # V = Vh.T -> note = different from MATLAB
    F = Vh[-1,:]
    F = np.reshape(F, (3,3))
    U, S, Vh = np.linalg.svd(F)
    S[-1] = 0
    F = U @ np.diagflat(S) @ Vh
    return F
    

# Takes h, w to handle boundary conditions
def apply_positional_noise(points, h, w, interval=3, ratio=0.2):
    print("done")
    """ 
    The goal of this function to randomly perturbe the percentage of points given 
    by ratio. This can be done by using numpy functions. Essentially, the given 
    ratio of points should have some number from [-interval, interval] added to
    the point. Make sure to account for the points not going over the image 
    boundary by using np.clip and the (h,w) of the image. 
    
    Key functions include but are not limited to:
        - np.random.rand
        - np.clip

    Arugments:
        points :: numpy array 
            - shape: [num_points, 2] ( note that it is <x,y> )
            - desc: points for the image in an array
        h :: int 
            - desc: height of the image - for clipping the points between 0, h
        w :: int 
            - desc: width of the image - for clipping the points between 0, h
        interval :: int 
            - desc: this should be the range from which you decide how much to
            tweak each point. i.e if interval = 3, you should sample from [-3,3]
        ratio :: float
            - desc: tells you how many of the points should be tweaked in this
            way. 0.2 means 20 percent of the points will have some number from 
            [-interval, interval] added to the point. 
    """
    ##################
    num_to_change = int(points.shape[0]*ratio)
    points[0:num_to_change] = points[0:num_to_change]+(np.random.uniform(-3, 3))
    points[:, 0] = np.clip(points[:, 0], 0, w)
    points[:, 1] = np.clip(points[:, 1], 0, h)
    ##################
    return points

# Apply noise to the matches. 
def apply_matching_noise(points, ratio=0.2):
    """ 
    The goal of this function to randomly shuffle the percentage of points given 
    by ratio. This can be done by using numpy functions. 
    
    Key functions include but are not limited to:
        - np.random.rand
        - np.random.shuffle  

    Arugments:
        points :: numpy array 
            - shape: [num_points, 2] 
            - desc: points for the image in an array
        ratio :: float
            - desc: tells you how many of the points should be tweaked in this
            way. 0.2 means 20 percent of the points will be randomly shuffled.
    """
    ##################
    #index into array and shuffle there, numpy will modify it
    # indeces = np.arange(points.shape[0])
    # indeces_to_shuffle = np.random.choice(indeces, points.shape[0]*ratio)
    
    index = int(ratio*points.shape[0])
    np.random.shuffle(points[:index])
    ##################
    return points


# Find the best fundamental matrix using RANSAC on potentially matching
# points
# 'matches_a' and 'matches_b' are the Nx2 coordinates of the possibly
# matching points from pic_a and pic_b. Each row is a correspondence (e.g.
# row 42 of matches_a is a point that corresponds to row 42 of matches_b.
# 'Best_Fmatrix' is the 3x3 fundamental matrix
# 'inliers_a' and 'inliers_b' are the Mx2 corresponding points (some subset
# of 'matches_a' and 'matches_b') that are inliers with respect to
# Best_Fmatrix.
def ransac_fundamental_matrix(matches_a, matches_b):
    # For this section, use RANSAC to find the best fundamental matrix by
    # randomly sampling interest points. You would reuse
    # estimate_fundamental_matrix() from part 2 of this assignment.
    # If you are trying to produce an uncluttered visualization of epipolar
    # lines, you may want to return no more than 30 points for either left or
    # right images.
    ##################
    # Your code here #
    ##################

    # Your ransac loop should contain a call to 'estimate_fundamental_matrix()'
    # that you wrote for part II.
    nmatches = matches_a.shape[0]
    N = 3000
    thresh = 0.01
    # placeholders, you can delete all of this
    best_F = np.zeros((3,3))
    ninliers = 0
    for i in range(N):
        shuffled_ind = np.arange(nmatches)
        np.random.shuffle(shuffled_ind)
        a = matches_a[shuffled_ind]
        b = matches_b[shuffled_ind]
        F = estimate_fundamental_matrix(a[0:9,:],b[0:9,:])
        dist_ind = np.zeros((nmatches))
        for row in range(nmatches):
            dist_ind[row] = abs(np.matmul(np.matmul(np.append(b[row].T, 1), F), np.append(a[row].T, 1)))
        inliers = np.sum(dist_ind < thresh)

        if (ninliers < inliers):
            ninliers = inliers
            inliers_b = b[dist_ind < thresh]
            inliers_a = a[dist_ind < thresh]
            best_F = F
        

    return best_F, inliers_a, inliers_b
