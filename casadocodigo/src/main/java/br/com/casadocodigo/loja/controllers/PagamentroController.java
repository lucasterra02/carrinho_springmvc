package br.com.casadocodigo.loja.controllers;

import java.util.Properties;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.DadosPagamento;

@RequestMapping("/pagamento")
@Controller
public class PagamentroController {

	@Autowired
	private CarrinhoCompras carrinho;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/finalizar", method = RequestMethod.POST)
	public Callable<ModelAndView> finalizar(RedirectAttributes model) {
		return () -> {
			String uri = "http://book-payment.herokuapp.com/payment";

			try {
                Properties props = System.getProperties(); 
                props.put("http.proxyHost","10.216.8.100"); 
                props.put("http.proxyPort","8080");

				String response = restTemplate.postForObject(uri, new DadosPagamento(carrinho.getTotal()),String.class);
				System.out.println(response);
				model.addFlashAttribute("sucesso", response);
				return new ModelAndView("redirect:/produtos");
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				model.addFlashAttribute("falha", "Valor maior que o permitido");
				return new ModelAndView("redirect:/produtos");
			}

		};

	}

}
