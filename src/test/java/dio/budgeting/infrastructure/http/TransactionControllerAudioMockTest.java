package dio.budgeting.infrastructure.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerAudioMockTest {

    @Test
    void deveProcessarArquivoComServicosSimulados() throws Exception {
        var persist = mock(PersistTransactionUseCase.class);
        var list = mock(ListTransactionsByCategoryUseCase.class);
        var transcription = mock(TranscriptionModel.class);
        var speech = mock(TextToSpeechModel.class);

        ChatClient.Builder builder =
                mock(ChatClient.Builder.class, RETURNS_SELF);

        ChatClient client = mock(ChatClient.class);

        ChatClient.ChatClientRequestSpec prompt =
                mock(ChatClient.ChatClientRequestSpec.class, RETURNS_SELF);

        ChatClient.CallResponseSpec response =
                mock(ChatClient.CallResponseSpec.class);

        when(builder.build()).thenReturn(client);
        when(client.prompt()).thenReturn(prompt);
        when(prompt.call()).thenReturn(response);

        when(transcription.transcribe(any(Resource.class)))
                .thenReturn("Gastei 80 reais na farmacia");

        when(response.content())
                .thenReturn("Transacao registrada");

        byte[] respostaSimulada =
                "AUDIO_SIMULADO".getBytes(StandardCharsets.UTF_8);

        when(speech.call("Transacao registrada"))
                .thenReturn(respostaSimulada);

        var controller = new TransactionController(
                persist,
                list,
                transcription,
                new ClassPathResource("prompts/system-message.st"),
                builder,
                speech
        );

        var mvc = MockMvcBuilders.standaloneSetup(controller).build();

        var arquivoAudio = new MockMultipartFile(
                "file",
                "teste.m4a",
                "audio/mp4",
                new byte[]{1, 2, 3, 4}
        );

        mvc.perform(multipart("/transactions/ai").file(arquivoAudio))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        containsString("audio.mp3")))
                .andExpect(content().bytes(respostaSimulada));

        verify(transcription).transcribe(any(Resource.class));
        verify(prompt).user("Gastei 80 reais na farmacia");
        verify(speech).call("Transacao registrada");
    }
}