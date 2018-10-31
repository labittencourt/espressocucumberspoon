package com.minhasAnotacoes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.minhasAnotacoes.support.util.CountingIdlingResourceListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {

    private EditText texto;
    private ImageView botaoSalvar;
    private static final String NOME_ARQUIVO = "arq_anotacao.txt";

    private static CountingIdlingResourceListener sIdlingNotificationListener;

    public static void setIdlingNotificationListener(CountingIdlingResourceListener idlingNotificationListener) {
        sIdlingNotificationListener = idlingNotificationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (EditText) findViewById(R.id.textoId);
        botaoSalvar = (ImageView) findViewById(R.id.btn_Salvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoDigitado = texto.getText().toString();
                gravarNoArquivo(textoDigitado);
                Toast.makeText(MainActivity.this, "Nota Salva com Sucesso", Toast.LENGTH_SHORT).show();
            }
        });


        // recuperar o que foi gravado
        if (lerArquivo() != null){
            texto.setText(lerArquivo());
        }
    }



    private void gravarNoArquivo(String texto){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(NOME_ARQUIVO, Context.MODE_PRIVATE));
            outputStreamWriter.write(texto);
            outputStreamWriter.close();

        }catch (IOException e) {
            Log.v("MainActivity", e.toString());
        }
    }

    private String lerArquivo(){
        String resultado = "";

        try {
            //abrir o arquivo
            InputStream arquivo = openFileInput(NOME_ARQUIVO);
            if (arquivo != null);

            //ler arquivo
            InputStreamReader inputStreamReader = new InputStreamReader( arquivo);

            // gerar um buffer do arquivo lido
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //recuperar texto do arquivo
            String linhaArquivo = "";
            while ((linhaArquivo = bufferedReader.readLine()) != null){
                resultado +=linhaArquivo;
            }

            arquivo.close();



        }catch (IOException e){
            Log.v("MainActivity", e.toString() );
        }
        return resultado;
    }


}
