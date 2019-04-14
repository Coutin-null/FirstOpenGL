package com.example.util;
import android.util.Log;

import static android.opengl.GLES30.*;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    public static int compileShader(int type,String shaderCode){
        //1.create shader object
        //2.link shader source code
        //3.compile shader object
        //4.output log info,deal with failed consequent
        //create a shader object depends on 'type',
        // if return value equals to 0 , failed
        final int shaderObjectId = glCreateShader(type);

        if(shaderObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new Shader");
            }
        }

        //parse shader source code to shader object
        glShaderSource(shaderObjectId,shaderCode);

        glCompileShader(shaderObjectId);

        //test shader compilation
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus , 0);

        //add shader object information to log
        if(LoggerConfig.ON){
            Log.v(TAG,"Result of compiling source:" + "\n" + shaderCode + "\n:"
            + glGetShaderInfoLog(shaderObjectId));
        }

        if(compileStatus[0] == 0){
            //if it failed ,delete the shader object
            glDeleteShader(shaderObjectId);

            if(LoggerConfig.ON){
                Log.w(TAG,"Compilation of shader failed.");
            }
        }
        return shaderObjectId;
    }
    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        //create a new program object
        final int programObjectId = glCreateProgram();

        if(programObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new program.");
            }
        }
        //attach two shader into program
        glAttachShader(programObjectId , vertexShaderId);
        glAttachShader(programObjectId , fragmentShaderId);

        //link(like compile in shader)
        glLinkProgram(programObjectId);

        //check
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);

        if(LoggerConfig.ON){
            Log.v(TAG,"Result of linking program:\n"+glGetProgramInfoLog(programObjectId));
        }

        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            if(LoggerConfig.ON){
                Log.w(TAG,"Linking of program failed.");

            }
        }
        return programObjectId;
    }
    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
        Log.v(TAG,"Results of validating program: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
    public static int buildProgram(String vertexShaderSource,
                                   String fragmentShaderSource){
        int program;

        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader,fragmentShader);

        if(LoggerConfig.ON){
            validateProgram(program);
        }

        return program;
    }

}
